/**
 * Created by Тимур on 08.05.2016.
 */
angular.module('vktrgtApp')
    .factory('VKCloudService', function ($rootScope, $cookies, $http, $q) {
        return {
            intersectGroups: function (ids, taskInfo) {
                return $http.post('/api/groups/users',
                    {
                        'names': ids.join(','),
                        'taskInfo': taskInfo
                    }, {
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                        transformRequest: function(data){
                            return $.param(data);
                        }
                    });
            }, groupsInfo: function (ids, taskInfo) {
                return $http.post('/api/groups/info/vk',
                    {
                        'names': ids.join(','),
                        'taskInfo': taskInfo
                    }, {
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                        transformRequest: function(data){
                            return $.param(data);
                        }
                    });
            }

        }

    })
