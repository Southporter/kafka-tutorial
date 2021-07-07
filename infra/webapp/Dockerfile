FROM python:3

WORKDIR /usr/src

RUN pip install --no-cache-dir flask==2.0.1 sqlalchemy==1.4.20 Flask-SQLAlchemy==2.5.1 pymysql==1.0.2

COPY app/ ./app

ENTRYPOINT FLASK_APP=app:app flask run --host=0.0.0.0