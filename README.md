# Kafka Tutorial

[![CC BY-SA 4.0][cc-by-sa-shield]][cc-by-sa]

A tutorial into the world of [Apache Kafka](https://kafka.apache.org).

### Introduction
This repo contains a few directories of code and config to allow one to
become more familiar with Kafka. Kafka can be a little daunting at first
with a lot of new jargon and lots of libraries. This repo aims to create
a fun, easy way to explore Apache Kafka.

If you find this in any way helpful, make sure to star the repository.
If you find an issues, please open an issue or fork the repo and create
a pull request with your fix.


### Supporting Infrastructure
The first folder you should explore is the Infra folder. In there you will
find a [docker compose](infra/docker-compose.yml) file that will let you spin
up a kafka cluster, and a few more pieces for this tutorial. This tutorial
assumes that you have [docker](https://www.docker.com/products/docker-desktop)
installed. You can also try using [podman](https://podman.io) with
[podman-compose](https://github.com/containers/podman-compose) but this is untested
and currently unsupported.

If you `cd` into the infra folder, you should be able to do a `docker-compose up`
command and have everything come up. This may take a while the first time, as
there are a lot of large images associated with Kafka.

Once you have the containers up and running, run the `setup.sh` file to create a few
topics and other resources needed for this tutorial.

### Part 1
Once you have the [supporting infrastructure](#supporting-infrastructure) up
you are ready to proceed to [Part 1](part1)


### Cleaning up
Once you are done, make sure you stop all containers and remove them:

```shell
# in infra/
docker-compose stop
docker-compose rm

# Remove unused images and volumes
# Cleans up space
docker system prune --all
```

---

This work is licensed under a
[Creative Commons Attribution-ShareAlike 4.0 International License][cc-by-sa].

[![CC BY-SA 4.0][cc-by-sa-image]][cc-by-sa]

[cc-by-sa]: http://creativecommons.org/licenses/by-sa/4.0/
[cc-by-sa-image]: https://licensebuttons.net/l/by-sa/4.0/88x31.png
[cc-by-sa-shield]: https://img.shields.io/badge/License-CC%20BY--SA%204.0-lightgrey.svg
