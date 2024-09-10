
from .app import app

# For running locally on poetry
def run_debug() -> None:
	app.run(debug=True, host="127.0.0.1")

def img_test():
    from .histograms import histograms
    from .db import db
    from . import ASSET_DIR
    hist = histograms(None, ASSET_DIR + "/cat1.jpg")
    hist.plot_histogram()

# For running deployed app on Gunicorn
if __name__ == '__main__':
	app.run()