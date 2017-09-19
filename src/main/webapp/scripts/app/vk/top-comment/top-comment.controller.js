/**users
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .controller('TopCommentController', function ($scope, VKCloudService, VKService) {

        $scope.submit = function () {
            VKCloudService.topComment($scope.users ? $scope.users.split('\n') : [],
                $scope.taskInfo, document.getElementById('file').files[0]).success(function(response){
                $scope.message = "Задача добавлена";
            }).error(function(response){
                $scope.message = "Произошла ошибка";
            })
        };
    });
