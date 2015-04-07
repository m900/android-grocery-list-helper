The purpose of this application is to keep a list of groceries while using the latest advancements on android mobile technology to locate and retrieve information on the  items listed on the phone application informing the user where is easy for them to gather their grocery list.

Green basket Android Application

**Let's you do your grocery shopping location based.**

**Let's you decide to go to the closest grocery store from where you are.**

**Let's you save the shopping list for later revision(Add item, edit item, and delete item).**

**Allows you to enter grocery items.**

**Let's you specify the quantity and the store of your choice.**

**Also, features a set of options to edit, delete and map items in the application.**


Green Basket basic hierarchy

  * SplashScreen
  * -MainActivity
    * -DbHelper
    * -Item
    * -EditListItemActivity
    * -MapSelectedActivity
      * ListItemMapActivity
> > > -MapMarkerOverlay
> > > -RoutePathOverlay
> > > -StoreLocation
> > > -Item
      * BalloonItemizedOverlay
> > > > -BalloonLayout

MainActivity
Purpose: Displays a list of items to shop using ListView with checkable options.

> Displays menus and controls the main functionality of the application to
> > add, remove, edit and map items on the phone screen.

DbHelper
Purpose: SQLite DB - creates tables to insert data on database.

Item
Purpose: Contains the data that will be stored in the Android device

> database and showed in the ListView - using the MainActivity.java

EditListItemActivity
Purpose: Helps to add and edit the items on the ListView from MainActivity.
> Uses bundles and ActivityResults to pass data among intents.

MapSelectedActivity
Purpose: Shows on screen the selected items and stores from a ListView on a Google Map.

ListItemMapActivity
Purpose: Displays a Google Map Activity on Android phone.
> Also, shows the current location and stores nearby with a route to them.

MapMarkerOverlay
Purpose: Adds and helps to display customized Markers - Green Baskets, Blue Baskets and Red Baskets
> on the Google Map view.

RoutePathOverlay
Purpose: Displays a red colored route on the screen using a List

&lt;GeoPoint&gt;

 to draw segments on the view.

StoreLocation
Purpose: Contains the data referencing where the ListItem is located
> with location store name and coordinates to serve the Google map service.

BalloonItemizedOverlay
Purpose:Purpose: Displays a Balloon Tip with information about places on the Google Map.

BalloonLayout
Purpose: Purpose: Structures the information displayed in the BalloonItemizedOverlay object.