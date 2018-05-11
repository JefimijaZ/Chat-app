angular.module('chatApp').controller('OverviewController', ['$scope', '$websocket', '$rootScope', '$http', '$state', 'UserService', function ($scope, $websocket, $rootScope, $http, $state, UserService) {
    var self = this;
    self.send = send;
    self.currentUser={};
    var ws = $websocket("ws://" + document.location.host + "/ChatAppWar/usersEndPoint");
    self.send("User," + $rootScope.globals.currentUser.username);

    function send(message) {
        if (angular.isString(message)) {
            ws.send(message);
        } else if (angular.isObject(message)) {
            ws.send(JSON.stringify(message));
        }
    }
    ws.onMessage(function (event) {
        console.log('message from overview: ', event.data);
        self.currentUser = JSON.parse(event.data);
        
    });
}]);