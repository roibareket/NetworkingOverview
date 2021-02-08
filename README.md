# NetworkingOverview

module: app
 - using interceptors for all network calls (like adding headers or printing to log)
 - using authenticator for handling 401. this will queue all calls to make only 1 refresh token call, when needed.
 
module: authentication
 - using AccountManager to store network tokens. you should never save them anywhere else, as it is not safe.
 - using AccountManager let you share tokens across apps you provide (need the same keystore)
 - AccountManager will not drop your credentials on clearData
