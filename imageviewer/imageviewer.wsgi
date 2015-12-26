import sys
import site
import os
import logging

logging.basicConfig(stream=sys.stderr)
#app_path = os.path.dirname(__file__)

sys.path.append("/Users/phanisaripalli/Projects/imageviewer")

from app import app as application
