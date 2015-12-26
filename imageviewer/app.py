from flask import Flask, render_template, request, redirect, url_for, flash
import os, sys
from os.path import abspath, dirname
from os import listdir
from werkzeug import secure_filename

app = Flask(__name__)

# configure upload folder name
app.config['UPLOAD_FOLDER'] = 'uploads/'

#configure project dir name
app.config['PROJECT_DIR'] = abspath(dirname(__file__))

# Upload DIR
#Fetches .../Projects/imageviewer/static/uploads/
app.config['UPLOAD_DIR'] = os.path.join(app.config['PROJECT_DIR'], 'static', 'uploads/' )


#Allowed formats
app.config['ALLOWED_FORMATS'] = set(['png', 'jpg', 'jpeg', 'JPEG', 'JPG', 'PNG'])

# Index/default page
@app.route('/')
@app.route('/index')
def index():
    #Fetch directory name 
    my_cwd = abspath(dirname(__file__))
    files = []
    allFiles = os.listdir(app.config['UPLOAD_DIR'])
    for aFile in allFiles:
        if not aFile.startswith('.'):
            files.append(aFile)
    
    return render_template('index.html', files = files, upload_dir = app.config['UPLOAD_DIR']);
    #return render_template('index.html', files = files, upload_dir = app.config['UPLOAD_DIR'], errors = errors);

# Upload function - To save Image, check if the image file extension is in allowed formats and
# check if same image file already exists otherwise store alerts as flash messages
@app.route('/upload',  methods=['POST', 'GET'])
def upload():
    if request.method == 'POST':
        if request.files:
            file = request.files['image']
            filename = secure_filename(file.filename)
            extension = filename.rsplit(".")[-1]
            if filename:
                if extension in  app.config['ALLOWED_FORMATS']:
                    filePath = os.path.join(app.config['PROJECT_DIR'],app.config['UPLOAD_DIR'], filename)
                    
                    if not os.path.isfile(filePath):
                        file.save(filePath)
                    else:   
                         flash('The image you are trying to upload already exists, please change the name or upload a different image')                         
                else:
                    flash('Bad file format, please choose a jpg/jpeg/png picture')
                    return redirect(url_for('index'))
            else:
                flash('Please upload a file')
    return redirect(url_for('index'))

# The name of the file is obtained through GET and is rmeoved from the uploads DIR
@app.route('/remove',  methods=['POST', 'GET'])
def remove():
    if request.args.get('filename'):
        fileName = os.path.join(app.config['PROJECT_DIR'],app.config['UPLOAD_DIR'], request.args.get('filename'))
        os.remove(fileName)
        flash('Removed ' + request.args.get('filename'))
    
    return redirect(url_for('index'))

@app.errorhandler(404)
def page_not_found(error):
    return render_template('page_not_found.html'), 404


# set the secret key.  keep this really secret:
app.secret_key = 'Of course it is a secret!'

if __name__ == '__main__':
    app.run(debug=True)
