angular.module('vktrgtApp')
    .factory('VKCloudService', function ($rootScope, $cookies, $http, $q) {
        return {
            intersectGroups: function (ids, taskInfo, file, minCount) {
                var fd = new FormData();
                fd.append('file', file);
                fd.append('names', ids.join(','))
                fd.append('taskInfo', taskInfo)
                fd.append('minCount', minCount)
                return $http.post('/api/groups/users',
                    fd, {
                        headers: {'Content-Type': undefined},
                        transformRequest: angular.identity
                    });
            }, intersectUsers: function (ids, taskInfo, min) {
                return $http.post('/api/users/leaders',
                    {
                        'users': ids.join(','),
                        'min': min,
                        'taskInfo': taskInfo
                    }, {
                        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                        transformRequest: function (data) {
                            return $.param(data);
                        }
                    });
            }, groupsInfo: function (ids, taskInfo) {
                return $http.post('/api/groups/info/vk',
                    {
                        'names': ids.join(','),
                        'taskInfo': taskInfo
                    }, {
                        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                        transformRequest: function (data) {
                            return $.param(data);
                        }
                    });
            }, getGroupActivity: function (activeAuditoryDTO) {
                return $http.post('/api/activity', activeAuditoryDTO);
            }, topicActivity: function (ids, minCount, taskInfo) {
                return $http.post('/api/topics',
                    {
                        'topicUrls': ids.join(','),
                        'minCount': minCount,
                        'taskInfo': taskInfo
                    }, {
                        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                        transformRequest: function (data) {
                            return $.param(data);
                        }
                    });
            }, groupMembers: function (groupId, taskInfo) {
                return $http.post('/api/groups/members',
                    {
                        'groupId': groupId,
                        'taskInfo': taskInfo
                    }, {
                        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                        transformRequest: function (data) {
                            return $.param(data);
                        }
                    });
            }, analyseUsers: function (model) {
                return $http.post('/api/analyse', model);
            }, filterUsers: function (model) {
                return $http.post('/api/filter', model);
            }

        }

    }
)
