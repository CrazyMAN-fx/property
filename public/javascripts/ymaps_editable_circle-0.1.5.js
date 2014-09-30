/*
 @global ymaps
 */
function editableCircleFactory(geometry, properties, options) {
    ymaps.util.augment(EditableCircle, ymaps.Circle, {
        setParent: function(parent) {
            if (!parent) {
                if (this.editor.state.get('editing')) this.editor.stopEditing();
                this.remove();
            }
            EditableCircle.superclass.setParent.call(this, parent);
            if (parent) {
                var self = this;
                if (this.geometry.getRadius() == 0) {
                    var radius = this.options.get('defaultRadius', 0) ? this.options.get('defaultRadius') :
                        Math.min(
                            Math.max(this.options.get('minRadius', 0), 5000000 / Math.pow(2, parent.getMap().getZoom())),
                            this.options.get('maxRadius', 0) ? this.options.get('maxRadius') : 100000000);
                    this.geometry.setRadius(radius);
                }
                this.getMap().events.add('actionend', this._renderCallback, this);
                this.geometry.events.add('pixelgeometrychange', this._renderCallback, this);
                this.render();
            }
        },
        editor: new GeometryEditorCircle(geometry),
        _renderCallback: function() {
            this.render();
        },
        render: function() {
            var pane = this.getMap().panes.get('overlays'),
                pixelGeometry = this.geometry.getPixelGeometry(),
                globalPixels = pixelGeometry.getCoordinates(),
                clientPixels = pane.toClientPixels(globalPixels);
        },
        getRadiusFormatted: function() {
            var radius = this.geometry.getRadius();
            return (radius > 1000) ? (Math.round(radius / 10)/100 + ' км') : Math.round(radius)+' м';
        },
        remove: function() {
            this.geometry.getMap().events.remove('actionend', this._renderCallback, this);
            this.geometry.events.remove('pixelgeometrychange', this._renderCallback, this);
            this._centerMark && this._centerMark.remove();
        }
    });
    return new EditableCircle(geometry, properties, options);
}
var EditableCircle = function(geometry, properties, options) {
    EditableCircle.superclass.constructor.call(this, geometry, properties, options);
    this._centerMark = null;
};

var GeometryEditorCircle = function(geometry, options) {
    this.geometry = geometry;
    this.events = new ymaps.event.Manager(geometry);
    this.options = new ymaps.option.Manager({}, options);
    this.state = new ymaps.data.Manager();
    this.state.set('editing', false);
    this.state.set('dragging', false);
    //private
    this._vertexBox = null;
    this._centerBox = null;
    this._radiusLine = null;

    // events
    var self = this;
    this._monitor = new ymaps.Monitor(this.state);
    this._monitor.
        add('editing', function(e) {
            if (self.state.get('editing')) {
                self._startEditing.call(self);
            }else {
                self._stopEditing.call(self);
            }
        });
    // private
};
GeometryEditorCircle.prototype.getPane = function() {
    return this.geometry.getMap().panes.get('overlays');
};
GeometryEditorCircle.prototype._createDomElements = function() {
    var $pane = $(this.getPane().getElement()),
        size = [10, 10],
        offset = [-6, -6],
        css = {
            position: 'absolute',
            width: size[0],
            height: size[1],
            marginLeft: offset[0],
            marginTop: offset[1],
            backgroundColor: 'white',
            border: '1px solid black',
            cursor: 'pointer',
            zIndex: 999
        },
        self = this;
    this._vertexBox = (this._vertexBox || $('<div></div>').
        css(css).
        bind('mouseenter', function(e) {
            $(this).css('backgroundColor', 'yellow')
        }).
        bind('mouseleave', function(e) {
            $(this).css('backgroundColor', 'white')
        }).
        bind('mousedown', function(e) {
            $(document).bind('mousemove', function(e) {
                e.stopPropagation();
                var map = self.geometry.getMap(),
                    pane = self.getPane(),
                    offset = $(pane.getElement()).offset(),
                    globalPixels = pane.fromClientPixels([e.pageX - offset.left, e.pageY - offset.top]),
                    geo = map.options.get('projection').fromGlobalPixels(globalPixels, map.getZoom()),
                    radius = Math.min(
                         Math.max(self.options.get('minRadius', 0), ymaps.coordSystem.geo.getDistance(self.geometry.getCoordinates(), geo)),
                            self.options.get('maxRadius', 0) ? this.options.get('maxRadius') : 100000000);
                self.geometry.setRadius(radius);
            }).
                bind('mouseup', function(e) {
                    $(this).unbind('mousemove mouseup');
                    document.ondragstart = document.body.onselectstart = null;
                    $(this).css('backgroundColor', 'white');
                    self.dragend();
                });
            document.ondragstart = document.body.onselectstart = function() {return false};
            $(this).css('backgroundColor', 'yellow');
            self.dragstart();
        })).
        appendTo($pane);
    self._radiusLine = (self._radiusLine || $('<div></div>').
        css({
            'position' : 'absolute',
            'height': '2px',
            'border-top': 'white 2px dashed',
            'zIndex': 800
        })).
        appendTo($pane);
    self._centerBox = (self._centerBox || $('<div></div>').
        css(css).
        bind('mouseenter', function(e) {
            $(this).css('backgroundColor', 'yellow')
        }).
        bind('mouseleave', function(e) {
            $(this).css('backgroundColor', 'white')
        })).
        bind('mouseenter', function(e) {
            $(this).css('backgroundColor', 'yellow')
        }).
        bind('mouseleave', function(e) {
            $(this).css('backgroundColor', 'white')
        }).
        bind('mousedown', function(e) {
            $(document).bind('mousemove', function(e) {
                e.stopPropagation();
                var map = self.geometry.getMap(),
                    pane = map.panes.get('overlays'),
                    offset = $(pane.getElement()).offset(),
                    globalPixels = pane.fromClientPixels([e.pageX - offset.left, e.pageY - offset.top]),
                    geo = map.options.get('projection').fromGlobalPixels(globalPixels, map.getZoom());
                self.geometry.setCoordinates(geo);
            }).
                bind('mouseup', function(e) {
                    $(this).unbind('mousemove mouseup');
                    document.ondragstart = document.body.onselectstart = null;
                    $(this).css('backgroundColor', 'white');
                    self.dragend();
                });
            document.ondragstart = document.body.onselectstart = function() {return false};
            $(this).css('backgroundColor', 'yellow');
            self.dragstart();
        }).
        appendTo($pane);
};
GeometryEditorCircle.prototype._clearDomElements = function() {
    this._centerBox.remove(); this._centerBox = null;
    this._vertexBox.remove(); this._vertexBox = null;
    this._radiusLine.remove(); this._radiusLine = null;
};
GeometryEditorCircle.prototype._startEditing = function() {
    this.geometry.getMap().events.add('actionend', this._renderCallback, this);
    this.geometry.events.add('pixelgeometrychange', this._renderCallback, this);
    this._createDomElements();
    this._render();
};
GeometryEditorCircle.prototype._stopEditing = function() {
    this._clearDomElements();
    this.geometry.getMap().events.remove('actionend', this._renderCallback, this);
    this.geometry.events.remove('pixelgeometrychange', this._renderCallback, this);
};
GeometryEditorCircle.prototype._renderCallback = function() {
    this._render.call(this);
};
GeometryEditorCircle.prototype._render = function() {
    var geometry = this.geometry,
        pane = this.getPane(),
        pixelGeometry = geometry.getPixelGeometry(),
        centerGlobal = pixelGeometry.getCoordinates(),
        centerClient = pane.toClientPixels(centerGlobal),
        boundsGlobal = pixelGeometry.getBounds(),
        vertexGlobal = [boundsGlobal[0][0], centerGlobal[1]],
        vertexClient = pane.toClientPixels(vertexGlobal),
        deg = Math.tan((vertexClient[0] - centerClient[0])*(vertexClient[1] - centerClient[1]));
    this._centerBox.css({left: centerClient[0]+'px', top: centerClient[1]+'px'});
    this._vertexBox.css({left: vertexClient[0]+'px', top: vertexClient[1]+'px'});
    this._radiusLine.css({
        width: Math.sqrt(Math.pow(vertexClient[0] - centerClient[0],2), Math.pow(vertexClient[1] - centerClient[1],2)),
        left: vertexClient[0] + 'px',
        top: vertexClient[1] + 'px',
        '-webkit-transform': 'rotate('+deg+'deg)',
        '-moz-transform': 'rotate('+deg+'deg)',
        '-ms-transform': 'rotate('+deg+'deg)',
        '-o-transform': 'rotate('+deg+'deg)',
        'transform': 'rotate('+deg+'deg)'});
};

GeometryEditorCircle.prototype.startEditing = function() {
    this.state.set("editing", true);
};
GeometryEditorCircle.prototype.stopEditing = function() {
    this.state.set("editing", false);
};
GeometryEditorCircle.prototype.dragstart = function() {
    this.state.set('dragging', true);
};
GeometryEditorCircle.prototype.dragend = function() {
    this.state.set('dragging', false);
};