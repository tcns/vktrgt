'use strict';

angular.module('vktrgtApp')
    .controller('SettingsController', function ($scope, Principal, Auth, Language, $translate, GoogleDrive, VKService) {
        $scope.success = null;
        $scope.error = null;
        Principal.identity().then(function(account) {
            $scope.settingsAccount = copyAccount(account);
        });

        $scope.driveToken = GoogleDrive.getToken();
        $scope.authDrive = function() {
            GoogleDrive.authorize().then(function(){
                $scope.driveToken = GoogleDrive.getToken();
            })
        }
        $scope.vkToken = VKService.getToken();
        $scope.authVk = function() {
            VKService.authorize().then(function(){
                $scope.vkToken = VKService.getToken();
            })
        }
        $scope.save = function () {
            Auth.updateAccount($scope.settingsAccount).then(function() {
                $scope.error = null;
                $scope.success = 'OK';
                Principal.identity(true).then(function(account) {
                    $scope.settingsAccount = copyAccount(account);
                });
                Language.getCurrent().then(function(current) {
                    if ($scope.settingsAccount.langKey !== current) {
                        $translate.use($scope.settingsAccount.langKey);
                    }
                });
            }).catch(function() {
                $scope.success = null;
                $scope.error = 'ERROR';
            });
        };

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login
            }
        }
    });
