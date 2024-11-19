# Electronic Payment System


## Authors

- [@lara-petkovic](https://www.github.com/lara-petkovic)
- [@dusan-sudjic](https://www.github.com/dusan-sudjic)
- [@milica-vujic](https://www.github.com/MilicaVujic)

# Application Configuration Table

| **App Name**   | **Configuration**     | **Details**                                                             |
|----------------|-----------------------|-------------------------------------------------------------------------|
| **WebShop**    | **Backend**           | ASP.NET Core API                                                       |
|                | **Backend Port (HTTP)**| `http://localhost:5275`                                                  |
|                | **Backend Port (HTTPS)**| `https://localhost:7098`                                                |
|                | **Frontend**          | Angular                                                                |
|                | **Frontend Port**     | `http://localhost:4200`                                                 |
|                | **Database**          | PostgreSQL                                                             |
|                | **Database Config**   | **Host**: `localhost`                                                   |
|                |                       | **Database**: `webshop`                                                |
|                |                       | **Username**: `postgres`                                               |
|                |                       | **Password**: `super`                                                  |
| **PaymentServiceProvider**   | **Backend**           | Spring                                                            |
|                | **Backend Port**      | `http://localhost:8085`                                                 |
|                | **Frontend**          | Angular                                                                  |
|                | **Frontend Port**     | `http://localhost:4201`                                                 |
|                | **Database**          | PostgreSQL                                                                  |
|                | **Database Config**   | **Host**: `localhost`                                                   |
|                |                       | **Database**: `psp`                                              |
|                |                       | **Username**: `postgres`                                                   |
|                |                       | **Password**: `super`                                            |
| **Bank**   | **Backend**           | Spring Boot API                                                        |
|                | **Backend Port**      | `http://localhost:8081`                                                 |
|                | **Frontend**          | Vue.js                                                                  |
|                | **Frontend Port**     | `http://localhost:3000`                                                 |
|                | **Database**          | MongoDB                                                                |
|                | **Database Config**   | **Host**: `localhost`                                                   |
|                |                       | **Database**: `taskmanager`                                            |
|                |                       | **Username**: `admin`                                                  |
|                |                       | **Password**: `adminpass`                                              |
| **Eureka Server**    | **Backend**           | Spring boot                                                       |
|                | **Backend Port (HTTP)**| `http://localhost:8761`                                                  |
| **Gateway PSP-WS**    | **Backend**           | Spring boot                                                      |
|                | **Backend Port (HTTP)**| `http://localhost:8086`                                                  |
| **Gateway PSP-Bank**    | **Backend**           | Spring boot                                                      |
|                | **Backend Port (HTTP)**| `http://localhost:8087`                                                  |
