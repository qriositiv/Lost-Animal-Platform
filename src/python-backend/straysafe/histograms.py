import cv2
import numpy as np
import os
import base64

import matplotlib.pyplot as plt
from matplotlib import colormaps


class histograms:
    def __init__(self, base64_image, source_image_path: os.PathLike = None) -> None:
        """
        Everything related to histograms is here.
        When initialized, the color and texture histograms are calculated.
        If the image is a face of a pet, the weights are adjusted accordingly.
        """
        self.weight_color = 0.5
        self.weight_texture = 0.5

        if source_image_path is not None:
            self.image = cv2.imread(source_image_path)
        else:
            image_data = base64.b64decode(base64_image)
            nparr = np.frombuffer(image_data, np.uint8)
            self.image = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

        self.color_hist = self.calc_color_hist(self.image)
        self.texture_hist = self.calc_texture_hist(self.image)

    def compare_hists(self, hist_list: list) -> dict:
        """
        Compare the color and texture histograms of the source image with the database histograms.
        :param record: The database records to compare the histograms with.
        :return: Dictionary of similarities between the source image and the database histograms.
        """
        similarity = {}
        if len(hist_list) != 0:
            for row in hist_list:
                image_id = row[0]
                db_texture_hist = np.load(row[2])
                db_color_hist = np.load(row[3])

                # Compare using Bhattacharyya distance for color and Chi-Squared distance for texture
                color_metric = cv2.compareHist(
                    self.color_hist, db_color_hist, cv2.HISTCMP_BHATTACHARYYA)
                texture_metric = cv2.compareHist(
                    self.texture_hist, db_texture_hist, cv2.HISTCMP_CHISQR)

                texture_metric_normalized = texture_metric / \
                    (1 + texture_metric)

                similarity[image_id] = self.weight_color * color_metric + \
                    self.weight_texture * texture_metric_normalized

        return similarity

    @staticmethod
    def lbp_image(source_image: np.ndarray) -> np.ndarray:
        """
        Calculate the Local Binary Pattern (LBP) of an image.
        :param source_image: The image to calculate the LBP for.
        """
        # Convert to grayscale
        gray = cv2.cvtColor(source_image, cv2.COLOR_BGR2GRAY)
        # Initialize new image with zeros that we will fill using LBP
        lbp = np.zeros_like(gray)
        # Consider each pixel in the image (excluding the borders)
        for i in range(1, gray.shape[0] - 1):
            for j in range(1, gray.shape[1] - 1):
                # Get the center pixel
                center = gray[i, j]
                # Binary pattern
                binary = ''
                binary += '1' if gray[i-1, j-1] > center else '0'
                binary += '1' if gray[i-1, j] > center else '0'
                binary += '1' if gray[i-1, j+1] > center else '0'
                binary += '1' if gray[i, j+1] > center else '0'
                binary += '1' if gray[i+1, j+1] > center else '0'
                binary += '1' if gray[i+1, j] > center else '0'
                binary += '1' if gray[i+1, j-1] > center else '0'
                binary += '1' if gray[i, j-1] > center else '0'
                # Convert binary string to decimal
                lbp[i, j] = int(binary, 2)
        return lbp

    @staticmethod
    def calc_texture_hist(image):
        """
        Calculate the histogram of the source image.
        :return: Texture histogram tuple to be further compared.
        """
        lbp_img = histograms.lbp_image(image)
        texture_hist = cv2.calcHist([lbp_img], [0], None, [256], [0, 256])
        cv2.normalize(texture_hist, texture_hist, alpha=0,
                      beta=1, norm_type=cv2.NORM_MINMAX)

        return texture_hist

    @staticmethod
    def calc_color_hist(image):
        """
        Calculate the histogram of the source image.
        :return: Color histogram tuple to be further compared.
        """
        color_hist = cv2.calcHist([image], [0, 1, 2], None, [
                                  32, 32, 32], [0, 256, 0, 256, 0, 256])
        cv2.normalize(color_hist, color_hist, alpha=0,
                      beta=1, norm_type=cv2.NORM_MINMAX)
        return color_hist

    def plot_histogram(self):
        fig = plt.figure()
        ax = fig.add_subplot(111, projection='3d')
        bin_edges = np.arange(32)
        x, y = np.meshgrid(bin_edges, bin_edges)

        # Flatten x, y for the bar3d function
        x = x.flatten()
        y = y.flatten()
        z = np.zeros_like(x)

        colormap = colormaps['Wistia']

        for i in range(32):
            # Get the current slice, flatten it, and normalize for plotting
            hist_values = self.color_hist[:, :, i].flatten()

            # Normalize the histogram values to the range [0, 1] for the colormap
            # Ensure no division by zero
            max_value = hist_values.max() if hist_values.max() > 0 else 1
            normalized_hist_values = hist_values / max_value

            # Retrieve RGBA colors from the colormap
            colors = colormap(normalized_hist_values)

            ax.bar3d(x, y, z, 1, 1, hist_values, color=colors, shade=True)

        ax.set_xlabel('Hue')
        ax.set_ylabel('Saturation')
        ax.set_zlabel('Value')
        plt.title('3D Color Histogram')
        plt.show()
