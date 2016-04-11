'use strict';

angular.module('vktrgtApp')
    .controller('BlogDetailController', function ($scope, $rootScope, $stateParams, entity, Blog) {
        $scope.blog = entity;
        $scope.load = function (id) {
            Blog.get({id: id}, function(result) {
                $scope.blog = result;
            });
        };
        var unsubscribe = $rootScope.$on('vktrgtApp:blogUpdate', function(event, result) {
            $scope.blog = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
