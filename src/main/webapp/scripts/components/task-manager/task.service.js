angular.module('vktrgtApp')
    .factory('TaskService', function ($rootScope, $cookies, $http, $q) {
        return {
            getTasks: function (params) {
                return $http.get('/api/tasks',
                    {params: params}, {
                        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                        transformRequest: function (data) {
                            return $.param(data);
                        }
                    });
            },
            removeTask: function (id) {
                return $http.delete('/api/tasks/'+id);
            }
        }
    }
)
