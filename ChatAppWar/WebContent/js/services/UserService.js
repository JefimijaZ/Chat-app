'use strict';

angular.module('chatApp').factory('UserService', ['$websocket', '$q', 'urls',
    function ($websocket, $q, urls) {
        var ws =new WebSocket("ws://"+"localhost:8080/ChatAppWar/rest/usersEndPoint");
       
        ws.onOpen(function () {
            console.log('connection open');
            ws.send('HELLO SERVER');
        });

        ws.onMessage(function (event) {
            console.log('message: ', event.data);
            var response;
           
            console.log(event.data);
        });
        ws.onClose(function (event) {
            console.log('connection closed', event);
        });
       

        var factory = {
            send: send,
        };

        return factory;

        function send(message) {
            if (angular.isString(message)) {
                ws.send(message);
            } else if (angular.isObject(message)) {
                ws.send(JSON.stringify(message));
            }
        }
    }
]);