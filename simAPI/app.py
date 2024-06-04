from flask import Flask, jsonify

app = Flask(__name__)

@app.route('/list', methods=['GET'])
def get_list():
    response_data = {"Example1": 1, "Example2": 2}
    return jsonify(response_data)

if __name__ == '__main__':
    app.run(debug=True)
