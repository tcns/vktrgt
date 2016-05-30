'use strict';

angular.module('vktrgtApp')
    .directive('activeMenu', function($translate, $locale, tmhDynamicLocale) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var language = attrs.activeMenu;

                scope.$watch(function() {
                    return $translate.use();
                }, function(selectedLanguage) {
                    if (language === selectedLanguage) {
                        tmhDynamicLocale.set(language);
                        element.addClass('active');
                    } else {
                        element.removeClass('active');
                    }
                });
            }
        };
    })
    .directive('dropdownLink', function() {
        return {
            restrict: 'A',
            transclude: false,
            link: function (scope, element, attrs) {

                $SIDEBAR_MENU = angular.element('#sidebar-menu');
                element.on('click', function(ev) {

                    var $li = element.parent();

                    if ($li.is('.active')) {
                        $li.removeClass('active');
                        angular.element('ul:first', $li).slideUp(function() {
                            scope.setContentHeight();
                        });
                    } else {
                        // prevent closing menu if we are on child menu
                        if (!$li.parent().is('.child_menu')) {
                            $SIDEBAR_MENU.find('li').removeClass('active');
                            $SIDEBAR_MENU.find('li ul').slideUp();
                        }

                        $li.addClass('active');

                        angular.element('ul:first', $li).slideDown(function() {
                            scope.setContentHeight();
                        });
                    }
                });
            }
        };
    })
    .directive('menuToggle', function() {
        return {
            restrict: 'A',
            transclude: false,
            link: function (scope, element, attrs) {
                $SIDEBAR_MENU = angular.element('#sidebar-menu');
                $LEFT_COL = angular.element('.left_col');
                $BODY = angular.element('body')
                element.on('click', function() {

                    if ($BODY.hasClass('nav-md')) {
                        $BODY.removeClass('nav-md').addClass('nav-sm');
                        $LEFT_COL.removeClass('scroll-view').removeAttr('style');

                        if ($SIDEBAR_MENU.find('li').hasClass('active')) {
                            $SIDEBAR_MENU.find('li.active').addClass('active-sm').removeClass('active');
                        }
                    } else {
                        $BODY.removeClass('nav-sm').addClass('nav-md');

                        if ($SIDEBAR_MENU.find('li').hasClass('active-sm')) {
                            $SIDEBAR_MENU.find('li.active-sm').addClass('active').removeClass('active-sm');
                        }
                    }

                    scope.setContentHeight();
                })
            }
        };
    })
    .directive('activeLink', function(location) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var clazz = attrs.activeLink;
                var path = attrs.href;
                path = path.substring(1); //hack because path does bot return including hashbang
                scope.location = location;
                scope.$watch('location.path()', function(newPath) {
                    if (path === newPath) {
                        element.addClass(clazz);
                    } else {
                        element.removeClass(clazz);
                    }
                });
            }
        };
    });
