import os
from flask import Flask, render_template
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = os.environ['DB_URI']
db = SQLAlchemy(app)


class User(db.Model):
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    username = db.Column(db.String(128), unique=True, nullable=False)


class Score(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    userId = db.Column(db.Integer, db.ForeignKey(User.id), unique=False, nullable=False, )
    score = db.Column(db.BIGINT)

    user = db.relationship('User', backref=db.backref('user'))

    def __repr__(self):
        return f'{self.user.username} - {self.score}'


db.create_all()
# Initialize DB with some dummy data
if len(Score.query.all()) == 0:
    user1 = User(username="beetles")
    user2 = User(username="van_halen")
    db.session.add(user1)
    db.session.add(user2)
    db.session.add(Score(user=user1, score="150"))
    db.session.add(Score(user=user2, score="450"))
    db.session.commit()


@app.route('/')
@app.route('/index')
def index():
    top_scores = Score.query.order_by(Score.score.desc()).limit(10).all()
    print(top_scores)
    return render_template("index.html", scores=top_scores)
