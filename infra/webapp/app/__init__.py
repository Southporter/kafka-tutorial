import os
from flask import Flask, render_template
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = os.environ['DB_URI']
db = SQLAlchemy(app)


class Score(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(128), unique=False, nullable=True)
    score = db.Column(db.BIGINT)

    def __repr__(self):
        return f'{self.username} - {self.score}'


db.create_all()
# Initialize DB with some dummy data
if len(Score.query.all()) == 0:
    db.session.add(Score(username="wormtail", score="150"))
    db.session.add(Score(username="snevilus", score="450"))
    db.session.commit()


@app.route('/')
@app.route('/index')
def index():
    top_scores = Score.query.order_by(Score.score.desc()).limit(10).all()
    print(top_scores)
    return render_template("index.html", scores=top_scores)
