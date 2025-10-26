### How to specify currency of flights returned, cabin class
### how is layover calculated, in the current leg or the next leg? and what is ground time￼

### how are taxes being calculated and what are different taxes? And chargeeBU and additionalTxnFeePub
### difference between craft and flight number
### Stop point times always coming as `0001-01-01T00:00:00`
### Response time almost 15 seconds
### isManual in booking response
### two fare fields in booking response
### how to divide fare between passengers for booking


## Booking Flow - 
### booking request traveler fare details would be taken from the fare quote response.
### Fare quote response would be saved to the cache with traceID as key. Then while building the booking request,it would be picked up from the cache and traveler fare details extracted from it.
### booking request passenger details to be saved to the database.