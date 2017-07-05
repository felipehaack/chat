## Chat - Real Time

### How to Setup
1. Download [Docker For Mac](https://docs.docker.com/docker-for-mac/) on your machine.
2. Install the Docker Application and make sure you have the latest version installed.
3. For other Operating systems, seek more information at [Docker Install](https://docs.docker.com/compose/install/).

### Architecture
The first approach to this application was Socket. However it was complicated to think of a way to scale the services when the connection between the clients falls down due to Socket problems.

The approach now is to use RabbitMQ. The client has two endpoints to send and retrieve messages.
1. `POST : /message`: The message is received by the service that has a connection to RabbitMQ and it delivers the message to the destination queue (the user that the message is trying to reach). (**Producer**)
2. `GET : /message`: The client calls the endpoint and the service retrieves all messages stored in your queue and returns it to the client. (**Consumer**)

With this architecture we can scale many producers and consumers, and also RabbitMQ.

### Initialize
1. Open the terminal
2. Clone the project: ```git clone https://github.com/felipehaack/chat.git```

### Backend
1. Go to the application folder ```cd chat-api```

#### How to Run - DEV
1. Run the following command: ```docker-compose up```
    - **The first launch of the API takes considerably more time than the RabbitMQ** due to the fact that it has to download dependencies, even when RabbitMQ has finished, the API will continue downloading.
2. Once the docker application has been launched successfully, go to  [localhost:9000](http://localhost:9000) just to make a warm up.

### How to Run - PROD
1. Run the following command: ```docker-compose -f docker-compose-prod.yml up -d```
    - **The first launch of the API takes considerably more time than the RabbitMQ** due to the fact that it has to download dependencies, even RabbitMQ has finished, the API will continue downloading.
2. In this case, you need to see the progress by looking at the container's logs by following command: `docker logs chatapi_api-prod_1`
    - It will be finish when the following line appears `(Server started, use Ctrl+D to stop and go back to the console...)`.
3. Once this line has appeared go to [localhost:9000](http://localhost:9000) just to make a warm up.

#### How to Test
1. Stop all applications: `docker stop $(docker ps -a -q)`
2. Run the following command: ```docker-compose -f docker-compose-test.yml up```
3. Once the application has been launched successfully, execute the following command: `docker exec -ti chatapi_api-test_1 bash`

##### Integration Test
1. Inside of the container, execute the following command: `sbt "project affin-api" "test"`
2. After finishing, execute `CTRL + C` to go back to the container.

##### Stress Test
1. Execute the following command: `sbt "project affin-gatling" "gatling:test"`
2. After finishing, execute `CTRL + C` to go back to the container.

##### Stress Test - Collected Results
The collected data **before** the warm up was:
```
================================================================================
---- Global Information --------------------------------------------------------
> request count                                      10000 (OK=9974   KO=26    )
> min response time                                      2 (OK=2      KO=250   )
> max response time                                  60018 (OK=11582  KO=60018 )
> mean response time                                   594 (OK=564    KO=12037 )
> std deviation                                       1587 (OK=868    KO=23411 )
> response time 50th percentile                        348 (OK=348    KO=554   )
> response time 75th percentile                        618 (OK=617    KO=1135  )
> response time 95th percentile                       1538 (OK=1533   KO=60014 )
> response time 99th percentile                       5207 (OK=4517   KO=60017 )
> mean requests/sec                                108.696 (OK=108.413 KO=0.283 )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          8224 ( 82%)
> 800 ms < t < 1200 ms                                1010 ( 10%)
> t > 1200 ms                                          740 (  7%)
> failed                                                26 (  0%)
---- Errors --------------------------------------------------------------------
> status.find.in(200,304,201,202,203,204,205,206,207,208,209), b     21 (80.77%)
ut actually found 500
> j.u.c.TimeoutException: Request timeout to not-connected after      4 (15.38%)
 60000ms
> j.u.c.TimeoutException: Request timeout to localhost/0:0:0:0:0      1 ( 3.85%)
:0:0:1:9000 after 60000ms
================================================================================
```

The collected data **after** the warm up was:
```
================================================================================
---- Global Information --------------------------------------------------------
> request count                                      10000 (OK=9939   KO=61    )
> min response time                                      3 (OK=3      KO=114   )
> max response time                                   3214 (OK=3214   KO=1947  )
> mean response time                                   414 (OK=413    KO=701   )
> std deviation                                        415 (OK=414    KO=378   )
> response time 50th percentile                        308 (OK=305    KO=643   )
> response time 75th percentile                        541 (OK=538    KO=839   )
> response time 95th percentile                       1283 (OK=1282   KO=1517  )
> response time 99th percentile                       2091 (OK=2094   KO=1946  )
> mean requests/sec                                204.082 (OK=202.837 KO=1.245 )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          8791 ( 88%)
> 800 ms < t < 1200 ms                                 629 (  6%)
> t > 1200 ms                                          559 (  6%)
> failed                                                21 (  1%)
---- Errors --------------------------------------------------------------------
> status.find.in(200,304,201,202,203,204,205,206,207,208,209), b     61 (100.0%)
ut actually found 500
================================================================================
```

### Frontend
1. Go to the application folder ```cd chat-web```

#### How to Run - DEV
1. Run the following command: ```docker-compose up web-dev```
2. Once the application has been launched successfully, go to [localhost:4200](http://localhost:4200).

#### How to Run - PROD
1. Run the following command: ```docker-compose up web-prod```
2. Once the application has been launched successfully, go to [localhost:4200](http://localhost:4200).