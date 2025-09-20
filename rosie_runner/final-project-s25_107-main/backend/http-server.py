from flask_cors import CORS
from datetime import datetime
from pickledb import PickleDB
from flask import Flask, request, jsonify
from werkzeug.security import generate_password_hash, check_password_hash

USER_INFO_DATABASE_NAME = "user-info"
EMAIL_TO_USERNAME_DATABASE_NAME = "email-to-username"
USER_INFO_DATABASE_FILE = "user-info.db"

LEADERBOARD_DATABASE_NAME = "leaderboard"
LEADERBOARD_DATABASE_FILE = "leaderboard.db"

server = Flask(__name__)
CORS(server)

@server.route("/delete-account", methods=["DELETE"])
def delete_account():
    data = request.get_json()
    if not data:
        return jsonify({"error": "No data provided."}), 400

    username_or_email = data.get("usernameOrEmail")
    password = data.get("password")
    
    if "@" in username_or_email:
        username = user_info_database.get(EMAIL_TO_USERNAME_DATABASE_NAME).get(username_or_email)
    else:
        username = username_or_email
        
    username_from_database = user_info_database.get(USER_INFO_DATABASE_NAME).get(username)  
    if username_from_database is None or not check_password_hash(username_from_database["password"], password):
        return jsonify({"error": "Invalid login information."}), 401
            
    username_database = user_info_database.get(USER_INFO_DATABASE_NAME)
    username_database.pop(username)
    user_info_database.set(USER_INFO_DATABASE_NAME, username_database)
    
    email_to_username_database = user_info_database.get(EMAIL_TO_USERNAME_DATABASE_NAME)
    email_to_username_database.pop(username_from_database["email"])
    user_info_database.set(EMAIL_TO_USERNAME_DATABASE_NAME, email_to_username_database)

    user_info_database.save()

    leaderboard = leaderboard_database.get(LEADERBOARD_DATABASE_NAME) or {}
    if username in leaderboard:
        leaderboard.pop(username)
        leaderboard_database.set(LEADERBOARD_DATABASE_NAME, leaderboard)
        leaderboard_database.save()

    return jsonify({"message": "Account deleted successfully."}), 200

@server.route("/sign-up", methods=["POST"])
def sign_up():
    data = request.get_json()
    if not data:
        return jsonify({"error": "No data provided."}), 400
    
    username = data.get("username")
    email = data.get("email")
    password = data.get("password")
    major = data.get("major")
    
    if user_info_database.get(USER_INFO_DATABASE_NAME).get(username) is not None:
        return jsonify({
            "code": "username_already_taken",
            "message": "Username already exists."
            }), 409
    
    if user_info_database.get(EMAIL_TO_USERNAME_DATABASE_NAME).get(email) is not None:
        return jsonify({
            "code": "email_already_taken",
            "message": "Email already exists."
            }), 409
    
    user_info = user_info_database.get(USER_INFO_DATABASE_NAME)
    user_info[username] = {
        "email": email,
        "password": generate_password_hash(password),
        "major": major
    }
    user_info_database.set(USER_INFO_DATABASE_NAME, user_info)
        
    email_to_username = user_info_database.get(EMAIL_TO_USERNAME_DATABASE_NAME)
    email_to_username[email] = username
    user_info_database.set(EMAIL_TO_USERNAME_DATABASE_NAME, email_to_username)

    user_info_database.save()
    return jsonify({"message": "User signed up successfully."}), 201

@server.route("/log-in", methods=["POST"])
def log_in():
    data = request.get_json()
    if not data:
        return jsonify({"error": "No data provided."}), 400
    
    username_or_email = data.get("usernameOrEmail")
    password = data.get("password")
    
    if "@" in username_or_email:
        username = user_info_database.get(EMAIL_TO_USERNAME_DATABASE_NAME).get(username_or_email)
    else:
        username = username_or_email
        
    username_from_database = user_info_database.get(USER_INFO_DATABASE_NAME).get(username)
    if username_from_database is None or not check_password_hash(username_from_database["password"], password):
        return jsonify({"error": "Invalid login information."}), 401
    
    return jsonify({"message": "User logged in successfully."}), 200

@server.route("/leaderboard", methods=["GET"])
def get_leaderboard():
    leaderboard = leaderboard_database.get(LEADERBOARD_DATABASE_NAME)
    return jsonify(leaderboard), 200

@server.route("/leaderboard", methods=["POST"])
def update_leaderboard():
    data = request.get_json()
    if not data:
        return jsonify({"error": "No data provided."}), 400
    
    username = data.get("username")
    score = data.get("score")
    
    user_info = user_info_database.get(USER_INFO_DATABASE_NAME).get(username)
    if user_info is None:
        return jsonify({"error": "User not found."}), 404
    
    major = user_info["major"]
    time_of_entry = datetime.now().isoformat()
    
    leaderboard = leaderboard_database.get(LEADERBOARD_DATABASE_NAME)
    if leaderboard is None:
        leaderboard = {}
    
    if username in leaderboard:
        existing_score = leaderboard[username].get("score")
        if score > existing_score:
            leaderboard[username] = {
                "username": username,
                "major": major,
                "score": score,
                "time": time_of_entry
            }
    else:
        leaderboard[username] = {
            "username": username,
            "major": major,
            "score": score,
            "time": time_of_entry
        }
    
    leaderboard_database.set(LEADERBOARD_DATABASE_NAME, leaderboard)
    leaderboard_database.save()
    
    leaderboard_list = list(leaderboard.values())
    leaderboard_list.sort(key = lambda x: (-x["score"], x["time"]))
    
    ranked_leaderboard = []
    for index, entry in enumerate(leaderboard_list):
        rank = index + 1
        ranked_leaderboard.append({**entry, "rank": rank})
    
    return jsonify(ranked_leaderboard), 200

def main(): 
    setup_databases()
    server.run(host="0.0.0.0", port=20061, debug=True)

def setup_databases():    
    setup_user_info_database()
    setup_leaderboard_database()
    
def setup_user_info_database():    
    global user_info_database
    user_info_database = PickleDB(USER_INFO_DATABASE_FILE)
    if user_info_database.get(USER_INFO_DATABASE_NAME) is None:
        user_info_database.set(USER_INFO_DATABASE_NAME, {})
        
    if user_info_database.get(EMAIL_TO_USERNAME_DATABASE_NAME) is None:
        user_info_database.set(EMAIL_TO_USERNAME_DATABASE_NAME, {})
        
def setup_leaderboard_database():
    global leaderboard_database
    leaderboard_database = PickleDB(LEADERBOARD_DATABASE_FILE)
    if leaderboard_database.get(LEADERBOARD_DATABASE_NAME) is None:
        leaderboard_database.set(LEADERBOARD_DATABASE_NAME, {})
    
main()