angular.module('chatApp').controller('RegisterController',['$state','$websocket',function($state, $websocket){
    var self = this;
    var ws = $websocket("ws://" + document.location.host + "/ChatAppWar/usersEndPoint");
    self.send = send;
    self.register;
    self.firstNamePattern=/^[A-Z][a-z]*\S$/;
    self.lastNamePattern=/^[A-Z][a-z]*\S$/;
    self.userNamePattern= /^\S*$/;
    self.passwordPattern = /^\S*$/;
    self.userData={};
    self.dataLoading = false;
    self.register = register;
    function register(){
        console.log("Register,"+self.userData.username+","+self.userData.password+","+self.userData.firstName+","+self.userData.lastName);
        self.send("Register,"+self.userData.username+","+self.userData.password+","+self.userData.firstName+","+self.userData.lastName);
        self.dataLoading = true;
    }
    
    function send(message) {
        if (angular.isString(message)) {
            ws.send(message);
        } else if (angular.isObject(message)) {
            ws.send(JSON.stringify(message));
        }
    }
    
    ws.onMessage(function (event) {
        console.log('message from overview: ', event.data);
        self.dataLoading = false;
        
    });
    ws.onClose(function (event) {
        console.log('connection closed', event);
        ws.send("Logout,"+$rootScope.globals.currentUser.username);
    });
}]);