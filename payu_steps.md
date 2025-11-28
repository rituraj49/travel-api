### PayU hosted checkout integration steps

#### When the user clicks on pay now button, the frontend calls backend api url with following payment details: 
amount, firstName, email, phone  in the request body.
#### The backend, after receiving the data will create data to be returned to the frontend containing these details - `key (merchantKey), txnid (transaction id), productinfo (product info), amount, firstname, email, phone, surl (success url), furl (failure url), hash ( created from the salt and other details)`.
#### The frontend will create a hidden form and submit it with the received fields as input fields in the form and POST action to payu endpoint - `test.payu.in/_payment`.
#### Payu will automatically show the html for the checkout page in the response where user can then make the payment.
#### After making the payment, payu calls the backend success or failure callback endpoint.
#### That endpoint will return a simple html to redirect to the frontend with teh transaction id and status and. User will only see redirecting message in the ui after the payment.