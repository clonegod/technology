from flask import Flask
from flask import request

app = Flask(__name__)

@app.route('/', methods=['GET', 'POST'])
def home():
    return '<h1>Home</h1>'

@app.route('/user-report/calc', methods=['POST'])
def signin():
    body = request.data
    print body
    return '<h3>%s</h3>' % body

if __name__ == '__main__':
    app.run(port=12345)
    
# curl -l -H "Content-type: application/json" -X POST -d '{"phone":"13521389587","password":"test"}' http://localhost:12345/user-report/calc
    
    