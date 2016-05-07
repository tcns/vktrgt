'use strict';

angular.module('vktrgtApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, ENV) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';
        $RIGHT_COL = angular.element('.right_col')
        $scope.setContentHeight = function () {
            // reset height
            $RIGHT_COL.css('min-height', $(window).height());

            var bodyHeight = $BODY.height(),
                leftColHeight = $LEFT_COL.eq(1).height() + $SIDEBAR_FOOTER.height(),
                contentHeight = bodyHeight < leftColHeight ? leftColHeight : bodyHeight;

            // normalize content
            contentHeight -= $NAV_MENU.height() + $FOOTER.height();

            $RIGHT_COL.css('min-height', contentHeight);
        };
        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };
    });
