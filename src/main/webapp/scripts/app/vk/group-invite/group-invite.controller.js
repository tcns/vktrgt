/**users
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('GroupInviteController', function ($scope, VKCloudService, VKService) {

        $scope.users = "";
        $scope.groupId = "130061815";
        $scope.token = VKService.getTokenStandalone();
        $scope.submit = function () {
            VKService.inviteUsers($scope.users ? $scope.users.split('\n'):[],  $scope.groupId, $scope, $scope.token);
        };
    });
