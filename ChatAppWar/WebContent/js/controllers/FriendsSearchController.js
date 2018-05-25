angular.module('chatApp').controller('FriendsSearchController', ['$scope', '$websocket', '$rootScope', '$state', '$stateParams',
    function ($scope, $websocket, $rootScope, $state, $stateParams) {
        console.log($stateParams.searchValue + "ovo su state params");
        var self = this;
        this.searchValue = $stateParams.searchValue;
        self.searchResults = {};
        self.friendsList = {};
        self.send = send;
        self.showAdd = showAdd;
        self.addFriend = addFriend;

        var ws = $websocket("ws://" + document.location.host + "/ChatAppWar/usersEndPoint");
        self.send("Search," + this.searchValue);
        self.send("getUserFriends," + $rootScope.globals.currentUser.username);
        function send(message) {
            if (angular.isString(message)) {
                ws.send(message);
            } else if (angular.isObject(message)) {
                ws.send(JSON.stringify(message));
            }
        }
        ws.onMessage(function (event) {
            console.log('message from search: ', event);
            var command = event.data.split("/")[0];
            if (command === "searchResults") {
                self.searchResults = JSON.parse(event.data.split("/")[1]);
            }else if(command === "friends"){
                self.friendsList = JSON.parse(event.data.split("/")[1]);
                console.log(self.friendsList);

            }
        });
        ws.onClose(function (event) {
            console.log('connection closed', event);
            ws.send("Logout," + $rootScope.globals.currentUser.username);
        });
        function showAdd(friendsUsername){
            for(var i =0; i<self.friendsList.length; i++){
                if(self.friendsList[i].username === friendsUsername){
                    return 0;
                }
            }
            
            return 1;
        }
        function addFriend(username, friendsUsername){
            console.log("addFriend," + friendsUsername + "," + username);
            self.send("addFriend," + friendsUsername + "," + username);
            //$state.go("home-abstract.profile-abstract.friends-abstract.friends-search",{username: $rootScope.globals.currentUser.username,searchValue: self.searchValue});
            $state.reload();
        }
    }
]);