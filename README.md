# Travelling Salesman
Offer the best price route to the client from city A to city B with the shortest duration.

To calculate it, we have a storage where we store all our one way flights from different origin and destination with the following details:

* Flight route: from X to Y
* Price of the flight
* Departure date time (local timezone)
* Arrival date time (local timezone)

### What the candidate should do
* Implement the service RouteCalculator.
* Add and implement any module/class/method needed for the purpose of the mission.
* Use the Flight interface to model the flight. Feel free to add any method that you need for your algorithm.
* Use the FlightStore interface to access to this storage and retrieve any data. Feel free to add any method that you need for your algorithm.
* Add any external dependency (if it's needed)
* (Optional) Add and implement the required Unit Tests to check the correctness of the functionality.

In the interview, the candidate will expose his/her code explaining the decisions and the assumptions taken during the implementation.

### What the candidate should NOT do
* Implement the Flight interface.
* Implement the FlightStore interface.
* Configure a dependency injection framework. Just annotate dependencies with @Inject.

### Some examples
#### Example 1
Input: BCN-NYC

Storage:

* BCN-NYC, 550€, 2022-01-13 12:00, 2022-01-13 15:00
* BCN-ORY, 100€, 2022-01-13 12:00, 2022-01-13 14:00
* ORY-NYC, 500€, 2022-01-13 15:00, 2022-01-13 18:00

Output: BCN-NYC
#### Example 2
Input: BCN-NYC

Storage:

* BCN-LON, 100€, 2022-01-13 12:00, 2022-01-13 15:00
* BCN-ORY, 100€, 2022-01-13 12:00, 2022-01-13 14:00
* LON-NYC, 500€, 2022-01-13 16:00, 2022-01-13 19:00
* ORY-NYC, 500€, 2022-01-13 15:00, 2022-01-13 18:00

Output: BCN-ORY, ORY-NYC
#### Example 3
Input: BCN-HND

Storage:

* BCN-MAD, 10€, 2022-01-13 12:00, 2022-01-13 14:00
* BCN-PAR, 50€, 2022-01-13 12:00, 2022-01-13 14:00
* PAR-LIS, 10€, 2022-01-13 15:00, 2022-01-13 17:00
* PAR-MNL, 50€, 2022-01-13 15:00, 2022-01-13 17:00
* BCN-SVQ, 100€, 2022-01-13 12:00, 2022-01-13 14:00
* MNL-HND, 500€, 2022-01-13 18:00, 2022-01-14 04:00
* MAD-LON, 10€, 2022-01-13 15:00, 2022-01-13 17:00
* WAW-NYC, 500€, 2022-01-13 21:00, 2022-01-14 02:00
* SVQ-WAW, 500€, 2022-01-13 15:00, 2022-01-13 20:00
* NYC-HND, 500€, 2022-01-14 03:00, 2022-01-14 08:00

Output: BCN-PAR, PAR-MNL, MNL-HND