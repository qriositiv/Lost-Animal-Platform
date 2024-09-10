from flask import jsonify, request, abort
import psycopg2

from .histograms import histograms
from .db import db

import cv2


def init_routes(app) -> jsonify:
    @app.route("/public/hello", methods=["GET"])
    def hello_world():
        """Test route to check if the server is running."""
        return jsonify({"message": "Hello, World!"}), 200

    @app.route("/public/compare", methods=["POST"])
    def compare_image() -> jsonify:
        """
        Compare the source image with the histograms in the database.
        """
        json_data = request.get_json()
        if not json_data:
            abort(400, "No JSON data provided")

        required_fields = ["source_id", "compare"]

        # Check for required fields in the parsed JSON
        for field in required_fields:
            if field not in json_data:
                abort(400, f"Missing {field} field")

        compare_id_list = json_data["compare"]
        source_id = json_data["source_id"]

        try:
            database = db()
            hist_list = database.fetch_hists(compare_id_list)
            source_image_path = database.fetch_image(source_id)
            hist = histograms(None, source_image_path)
            similarity = hist.compare_hists(hist_list)
            database.insert_color_histogram(source_id, hist.color_hist)
            database.insert_texture_histogram(source_id, hist.texture_hist)

            # Format and return the similarity data
            similarity_json = [
                {"id": key, "value": value} for key, value in similarity.items()
            ]
            return jsonify({"similarity": similarity_json, "source_id": source_id}), 200
        except (Exception, psycopg2.Error) as error:
            return jsonify({"error": str(error)}), 500

    @app.route("/public/quick-compare", methods=["POST"])
    def quick_compare() -> jsonify:
        """
        Compare the source image with the histograms in the database.
        """
        json_data = request.get_json()
        if not json_data:
            abort(400, "No JSON data provided")

        required_fields = ["image", "compare"]

        # Check for required fields in the parsed JSON
        for field in required_fields:
            if field not in json_data:
                abort(400, f"Missing {field} field")

        base64_image = json_data["image"]
        compare_id_list = json_data["compare"]

        try:
            database = db()
            hist_list = database.fetch_hists(compare_id_list)
            similarity = histograms(base64_image).compare_hists(hist_list)

            # Find the first report that matches the pet id, because we filter out duplicates
            pet_to_report_id = {}
            for entry in compare_id_list:
                pet_id = entry['petId']
                report_id = entry['reportId']
                if pet_id not in pet_to_report_id:
                    pet_to_report_id[pet_id] = report_id

            # Create the similarity_json list
            similarity_json = [
                {
                    "reportId": pet_to_report_id.get(key, ""),
                    "similarity": value
                }
                for key, value in similarity.items()
            ]

            return jsonify(similarity_json), 200
        except (Exception, psycopg2.Error) as error:
            return jsonify({"error": str(error)}), 500
