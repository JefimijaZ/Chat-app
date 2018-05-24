angular.module('chatApp').controller('FriendsListController', ['$scope', '$websocket', '$rootScope', '$http', '$state', 'UserService', function ($scope, $websocket, $rootScope, $http, $state, UserService) {
    var self = this;
    self.send = send;
    self.friendsList={};
    self.deleteFriend = deleteFriend;
    var ws = $websocket("ws://" + document.location.host + "/ChatAppWar/usersEndPoint");
    self.send("getUserFriends," + $rootScope.globals.currentUser.username);

    function send(message) {
        if (angular.isString(message)) {
            ws.send(message);
        } else if (angular.isObject(message)) {
            ws.send(JSON.stringify(message));
        }
    }
    ws.onMessage(function (event) {
        console.log('message from friends list: ', event.data);
        var command = event.data.split("/")[0];
            if (command === "friends") {
                self.friendsList = JSON.parse(event.data.split("/")[1]);
            }
        
    });
    ws.onClose(function (event) {
        console.log('connection closed', event);
        ws.send("Logout,"+$rootScope.globals.currentUser.username);
    });
    function deleteFriend(username,friendsUsername){
        self.send("removeFriend," + friendsUsername + "," + username);
        $state.reload();
    }
}]);