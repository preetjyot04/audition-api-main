# Run Docker Image of Prometheus

1. Download and install Docker from link - https://docs.docker.com/engine/install/
2. one command Prompmt and Run below commands. 
   ````
   docker pull prom/prometheus
   docker image ls
   docker run -d -p 9090:9090 -v <PATH_OF_YOUR_WORKSPACE>/audition-api-main/monitoring/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus


## Start Prometheus
1. Once the Prometheus is running, You can open the Prometheus with URL - http://localhost:9090/
2. Search for any metrics coming under /prometheus end point of you application For eg. 'application_started_time_seconds'

### Setup Grafana Dashboard

- Open command prompt under datasources folder of monitorining (audition-api-main>monitoring>grafana>provisioning>datasources) and run below commands.
 ````
docker-compose up
````
- Strat Grafana is browser - http://localhost:3003/
- Default username and password is  admin. 
- You can see the grafana dashboards. 

### Note
- No need to create datasources, since we have provided datasources info while running the grafa. 
- You can install the docker desktop as well for UI interface. (optional but useful to see the status in UI);

