angular.module('chatApp').controller('InboxController', ['$scope', '$websocket', '$rootScope', '$state', '$stateParams',
    function ($scope, $websocket, $rootScope, $state, $stateParams) {
        var self = this;
        self.recipient = "";
        self.message = "";

        self.conversationList = {};
        self.conversations = [];
        self.send = send;
        self.newConversation = newConversation;
        // self.conversationList = conversationList;
        var ws = $websocket("ws://" + document.location.host + "/ChatAppWar/usersEndPoint");
        ws.send("getConversations," + $rootScope.globals.currentUser.username);

        function send(message) {
            if (angular.isString(message)) {
                ws.send(message);
            } else if (angular.isObject(message)) {
                ws.send(JSON.stringify(message));
            }
        }
        ws.onMessage(function (event) {
            console.log('message from inbox-abstract: ', event);
            var command = event.data.split("/")[0];
            if (command === "conversations") {
                self.conversations = [];
                self.conversationList = JSON.parse(event.data.split("/")[1]);
                Loop1:
                for (var i = 0; i < self.conversationList.length; i++) {
                    if (self.conversationList[i].user.username === $rootScope.globals.currentUser.username) {
                        for (var j = 0; j < self.conversations.length; j++) {
                            Loop2:
                            if (self.conversationList[i].friend.username === self.conversations[j].username) {
                                break Loop1;
                            }

                        }
                        self.conversations.push(self.conversationList[i].friend);
                    } else if (self.conversationList[i].friend.username === $rootScope.globals.currentUser.username) {
                        for (var j = 0; j < self.conversations.length; j++) {
                            if (self.conversationList[i].user.username === self.conversations[j].username) {
                                break Loop1;
                            }

                        }
                        self.conversations.push(self.conversationList[i].user);
                    }
                }
            } else if (command === "friends") {
                self.friendsList = JSON.parse(event.data.split("/")[1]);
                console.log(self.friendsList);

            }
        });
        ws.onClose(function (event) {
            console.log('connection closed', event);
            ws.send("Logout," + $rootScope.globals.currentUser.username);
        });

        function newConversation() {
            console.log("Inbox controller: " + "newConversation," + $rootScope.globals.currentUser.username + "," + self.recipient + "," + self.message);
            self.send("newConversation," + $rootScope.globals.currentUser.username + "," + self.recipient + "," + self.message);
        }

    }
]);

