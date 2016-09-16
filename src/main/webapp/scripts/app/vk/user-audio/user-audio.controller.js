/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('UserAudioController', function ($scope, VKCloudService, VKService) {
        var test = false;
        function initTestData() {
            "use strict";
            $scope.users =
                "6270955\n" +
                "11167295\n" +
                "11466629\n" +
                "41887667\n" +
                "23025555";
            $scope.audio = "james blake";
        }
        if (test) {
            initTestData();
        }
        $scope.submit = function () {

            VKService.searchAudio($scope.users ? $scope.users.split('\n'):[],
                $scope.audio ? $scope.audio.split('\n'):[],
                $scope).then(function(ret){
                    "use strict";
                    console.log(ret);
                    var str = _.reduce(ret, function(memo, group) {
                        return memo+group+"\n";
                    }, "");
                    VKService.export(str, "audio-search");
            })
        };
    });
