import psycopg2
from dotenv import load_dotenv
import os
import numpy as np
from .histograms import histograms

import cv2

load_dotenv()

DIR_COLOR_HISTOGRAMS = os.path.abspath(
    "/home/www-data/.straysafe/color-histograms")
DIR_TEXTURE_HISTOGRAMS = os.path.abspath(
    "/home/www-data/.straysafe/texture-histograms")
DIR_PHOTOS = os.path.abspath("/home/www-data/.straysafe/pet-photos")


class db:
    def __init__(self) -> None:
        self.connection = psycopg2.connect(
            user=os.getenv("DB_USERNAME"),
            password=os.getenv("DB_PASSWORD"),
            host=os.getenv("DATABASE_IP"),
            port=os.getenv("DB_PORT"),
            database=os.getenv("DB_NAME"),
        )
        self.cursor = self.connection.cursor()

        directories = [DIR_COLOR_HISTOGRAMS, DIR_TEXTURE_HISTOGRAMS]

        for directory in directories:
            if not os.path.exists(directory):
                os.makedirs(directory)
                print(f"Created directory: {directory}")

    def fetch_hists(self, compare_id_list: list):
        """
        Fetch the color and texture histograms from the database for the given pet IDs.
        :param compare_id_list: A list of dictionaries with reportId and petId keys.
        :return: The color and texture histograms for the given pet IDs.
        """
        pet_ids = tuple(set(item['petId'] for item in compare_id_list))
        if not pet_ids:
            return []

        query = "SELECT image_id, image, texture_histogram, color_histogram FROM image WHERE image_id IN %s"
        self.cursor.execute(query, (pet_ids,))
        hist_list = self.cursor.fetchall()

        changed = False
        for hist in hist_list:
            if not hist[2]:
                img = cv2.imread(hist[1])
                text_hist = histograms.calc_texture_hist(img)
                self.insert_texture_histogram(hist[0], text_hist)
                changed = True
            if not hist[3]:
                img = cv2.imread(hist[1])
                color_hist = histograms.calc_color_hist(img)
                self.insert_color_histogram(hist[0], color_hist)
                changed = True

        if changed:
            query = "SELECT image_id, image, texture_histogram, color_histogram FROM image WHERE image_id IN %s"
            self.cursor.execute(query, (pet_ids,))
            hist_list = self.cursor.fetchall()

        return hist_list

    def fetch_image(self, image_id: int) -> np.ndarray:
        """
        Fetch the image from the database.
        :param image_id: The ID of the image to fetch.
        :return: The image as a numpy array.
        """
        query = "SELECT image_path FROM image WHERE image_pet_id = %s"
        self.cursor.execute(query, (image_id,))
        return self.cursor.fetchone()[0]

    def insert_texture_histogram(self, image_id, text_hist):
        hist_texture_path = os.path.join(
            DIR_TEXTURE_HISTOGRAMS, f"{image_id}.npy")

        np.save(hist_texture_path, text_hist)

        query = """
        UPDATE image
        SET texture_histogram = %s
        WHERE image_id = %s
        """
        self.cursor.execute(
            query, (hist_texture_path, image_id))
        self.connection.commit()

    def insert_color_histogram(self, image_id, color_hist):

        hist_color_path = os.path.join(
            DIR_COLOR_HISTOGRAMS, f"{image_id}.npy")

        np.save(hist_color_path, color_hist)

        query = """
        UPDATE image
        SET color_histogram = %s
        WHERE image_id = %s
        """
        self.cursor.execute(
            query, (hist_color_path, image_id))
        self.connection.commit()

    def insert_images(self, image_paths: os.PathLike) -> None:
        """Insert an image into the PostgreSQL database for testing purposes"""
        for i, image in enumerate(os.listdir(image_paths)):
            image_path = os.path.abspath(os.path.join(image_paths, image))

            query = "INSERT INTO image (image_pet_id, image_path) VALUES (%s, %s)"
            self.cursor.execute(
                query,
                (
                    str(i),
                    image_path,
                ),
            )
            self.connection.commit()
            print("Image inserted:", image_path)

    def __del__(self):
        self.cursor.close()
        self.connection.close()
