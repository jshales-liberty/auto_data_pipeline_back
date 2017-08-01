#Hive

#Table of Contents

#Introduction
Hive is an automotive fleet management software application.  It simulates the availability of real time vehicle intelligence for any business that operates a fleet of vehicles.  Hive’s real time data includes vehicle location, incident notification, and vehicle movement activity.  In addition to real-time data, Hive also provides dashboards which track historical metrics such as vehicle breakdowns, vehicle miles traveled, and other vehicle activities.  Hive also has a vehicle and driver administration tool, which allows the user to manage their fleet within an intuitive user interface.

#Data Set
As a proof of concept, a historical dataset of taxicab GPS data was used as a proxy for data from a user's actual fleet of vehicles.  The dataset includes 30 days of GPS location data for approximately 500 taxicabs.  Additional fields, such as delivery status, were added to the dataset for chart creation purposes.  Further details regarding the original dataset are available <a href= "http://crawdad.org/epfl/mobility/20090224/">here</a>.

#Front End/User Experience
In order to secure the user's vehicle data, the application requires a login by the user.  Once logged into the application, the user has access to the map, dashboards, and driver management tools.  A live demo of the application is available here.  For demonstration purposes, the login has been disabled, allowing any visitor to experience the application.  Some highlights of the application's front end features, and an overview of their technical workings, are described below.

##Vehicle Location and Metrics
- **Map of current vehicle location** - The real time location of all fleet vehicles are shown on a map, giving the user an immediate overview of the distribution of vehicles across a given area.  In order to quickly notify the user of a vehicle incident (a breakdown, an accident, etc.), the vehicle marker will change color if an incident occurs.  In addition, the vehicle map markers are interactive--clicking on a given vehicle on the map instantly provides further information about the vehicle and its driver.  The location of the vehicles is simulated using a table of vehicle location data.  Each location record includes a timestamp, which has been modified to reflect a presently occurring timeframe.  An API call to the location data occurs every 30 seconds and returns to the map the most recently occurring location record for each vehicle.

- **Chart of number of deliveries by day of week** - The number of deliveries for each vehicle is summed by day of the week and charted to show trending.  The chart allows the user to compare two vehicles against each other, or against the average for the total fleet.  This chart could be used for resource planning and allocation, or implementing operational improvements within the fleet.

- **Chart of average number of miles traveled per day** - The vehicle miles traveled for each vehicle is summed by day of the week and charted to show trending.  The chart allows the user to compare two vehicles against each other, or against the average for the total fleet.  The trends shown in this chart can be used to optimize delivery routes among vehicles, or identify peak days of travel.  The <a href = "https://en.wikipedia.org/wiki/Haversine_formula">Haversine formula</a> was used to calculate the distance between each location record.  While this formula measures distance as the shortest route between two points ("as the crow flies"), rather than along existing roadways, it is still an effective measure for a proof of concept.  The limitation here is a function of the source data, not the algorithm.  In other words, with more granular location data (perhaps recorded every second), the Haversine formula would be able to calculate a more accurate measure of miles traveled.

##Vehicle and Driver Administration
Hive includes functionality to manage a fleet of vehicles.  Features include the ability to view, update, create, or delete vehicles and their associated drivers.  From driver administration page, the user can view, sort and search within current drivers and their vehicles, as well as export data in tabular or PDF formats.  Hive is also integrated with <a href="http://www.carqueryapi.com/">CarQuery</a>, an API that retrieves updated and detailed vehicle information regarding year, make, model, and other vehicle-specific data points.  This integration ensures that vehicle data is consistent and accurate as the makeup of the fleet changes over time.

#External APIs
**Google Maps** - Provided the base map for the real time vehicle locations.
**Carquery** - Provided vehicle year, make, and model data.  User selects these variables when creating a new vehicle in the application.
