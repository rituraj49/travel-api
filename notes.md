### Authentication differentiate between test and live environments.
### How to specify currency of flights returned, cabin class
### how is layover calculated, in the current leg or the next leg? and what is ground time￼

### how are taxes being calculated and what are different taxes? And chargeeBU and additionalTxnFeePub
### difference between craft and flight number - add request body in excel
### Stop point times always coming as `0001-01-01T00:00:00`
### Response time almost 15 seconds
### isManual in booking response
### two fare fields in booking response
### how to divide fare between passengers for booking
### price and tax fields to be included for each passenger fare in booking and whether yqTax and pgCharge would be divided among the travelers or added to each traveler.
### Id and journeyWayType in ticket response passenger barcode section
### Why is there an invoice details array and invoice outside the array also
### what is invoice status 3
### booking response null at times
### booking status coming as 5 for ticketing done lcc flight and 1 for ticketing not done non lcc flight. Means if ticketing is done, then status is InProgress and if booking is done and ticketing is still pending then the status is Successful.
### Booking response gives - `errorCode=35, errorMessage=Booking failed from supplier side.`. 
### On manually fetching booking details from GetBookingDetails api with trace id, it shows - ` "ErrorCode": 28,
            "ErrorMessage": "Your request could not be processed as booking is under process."`

## Booking Flow - 
### booking request traveler fare details would be taken from the fare quote response.
### Fare quote response would be saved to the cache with traceID as key. Then while building the booking request,it would be picked up from the cache and traveler fare details extracted from it.
### booking request passenger details to be saved to the database.

### Should ui decide whether to call booking or ticketing based on lcc/non lcc or the should the backend decide. If ui just sends the book/ticket request body, then for ticketing to be done payment is needed beforehand.