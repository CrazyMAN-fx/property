function CarouselManger(container, data){
            this.data = data;
            this.container=container;
            this.contentDivResized=function(){};
            this.navigationDivResized=function(){
                    var width = this.navigationDiv.innerWidth();
                    if (width >= 300) {
                        width = width / 3;
                    } else if (width >= 200) {
                        width = width / 2;
                    }

                    this.navigationDiv.jcarousel('items').css('width', width + 'px');
            };
            this.contentItemUnFocus=function(index){};
            this.contentItemFocus=function(index){};
            this.navigationItemShown=function(index){
                var indexes=[];
                var prev = index-1;
                var next = index+1;
                if (prev>-1){
                    indexes.push(prev);
                }
                if (next<this.data.length){
                    indexes.push(next);
                }
                indexes.push(index);
                var navigationDivLi;
                var rowData;
                for (var i=0; i<indexes.length; i++){
                    navigationDivLi=this.navigationDivLis[indexes[i]];
                    if (navigationDivLi.initialized===undefined){
                        rowData=this.data[indexes[i]];
                        $(navigationDivLi).find('img').attr("src",rowData[1]);
                        navigationDivLi.initialized=true;
                    }
                }
            };
            this.contentDiv = container.find('.content');
            this.navigationDiv = container.find('.navigation');
            this.prevControl = container.find('.jcarousel-prev');
            this.nextControl = container.find('.jcarousel-next');
            this.currContentItem=-1;
            this.initContentDivLi=function(){
                var fragment = document.createDocumentFragment();
                var contentDivUl = this.contentDiv.find('ul');
                var contentDivLiTemplate=contentDivUl.find('li');

                for (var i = 1; i < this.data.length; i++) {
                    fragment.appendChild(contentDivLiTemplate[0].cloneNode(true));
                }

                if (data.length>0){
                    contentDivLiTemplate.addClass('active');
                    contentDivUl[0].appendChild(fragment);
                } else {
                    contentDivLiTemplate.remove();
                }

                this.contentDivLis = this.contentDiv.find('li');
            };
            this.initNavigationDivLi=function(){
                var fragment = document.createDocumentFragment();
                var navigationDivUl = this.navigationDiv.find('ul');
                var navigationDivLiTemplate=navigationDivUl.find('li');

                for (var i = 1; i < this.data.length; i++) {
                    fragment.appendChild(navigationDivLiTemplate[0].cloneNode(true));
                }

                if (data.length>0){
                    navigationDivLiTemplate.addClass('active');
                    navigationDivUl[0].appendChild(fragment);
                } else {
                    navigationDivLiTemplate.remove();
                }

                this.navigationDivLis = this.navigationDiv.find('li');
            };

            this.initialize = function(){
                var instance = this;

                this.initContentDivLi();
                this.initNavigationDivLi();

                this.contentDiv.on('jcarousel:create jcarousel:reload', function() {
                    instance.contentDivResized();
                 }).jcarousel({
                    vertical: false
                });
                this.navigationDiv.on('jcarousel:create jcarousel:reload', function() {
                    instance.navigationDivResized();
                }).jcarousel({
                    vertical: false
                });

                this.prevControl.jcarouselControl({
                    target: '-=1',
                    carousel: this.navigationDiv
                });
                this.nextControl.jcarouselControl({
                    target: '+=1',
                    carousel: this.navigationDiv
                });

                $.each(this.navigationDiv.jcarousel('items'),function(index, item) {
                    $(item).click(function() {
                        instance.contentItemUnFocus(instance.currContentItem);
                        instance.currContentItem=index;
                        instance.contentItemFocus(index);
                    });
                });
                this.navigationDiv.on('jcarousel:firstin', 'li', function(event, carousel) {
                    instance.navigationItemShown(instance.navigationDiv.jcarousel('items').index(this));
                });
                this.navigationDiv.on('jcarousel:lastin', 'li', function(event, carousel) {
                    instance.navigationItemShown(instance.navigationDiv.jcarousel('items').index(this));
                });

                this.navigationItemShown(0);
                if (this.data.length>=3){
                    this.navigationItemShown(2);
                }
                this.contentItemFocus(0);
            };
};

function ImageCarouselManger(container, data){
    var carouselManger = new CarouselManger(container, data);
    carouselManger.contentItemFocus=function(index){
            var contentDivLi=this.contentDivLis[index];
            if (contentDivLi.initialized===undefined){
                var rowData=this.data[index];
                $(contentDivLi).find('img').attr("src",rowData[0]);
                $(contentDivLi).find('p').text(rowData[2]);
                contentDivLi.initialized=true;
            }
            this.contentDiv.jcarousel('scroll', index, true);
    };
    carouselManger.contentDivResized=function(width){
        this.contentDiv.jcarousel('items').css('width', this.contentDiv.innerWidth() + 'px');
    };
    return carouselManger;
};

function PanoCarouselManger(container, data){
    var carouselManger = new CarouselManger(container, data);
    carouselManger.contentItemFocus=function(index){
        var viewer;
        var contentDivLi=this.contentDivLis[0];
        var rowData=this.data[index];
        if (contentDivLi.initialized===undefined){
            $(contentDivLi).find('p').text(rowData[2]);
            viewer = new PanoradoJS($(contentDivLi).find('canvas')[0]);
            viewer.controls.visible=false;
            var imgs = [];
            for (var i=0; i<data.length; i++){
                rowData = this.data[i];
                imgs.push({ src: rowData[0], projection: 'spherical' });
            }
            viewer.imageList = imgs;
            contentDivLi.viewer=viewer;
            contentDivLi.initialized=true;
        } else {
            viewer=contentDivLi.viewer;
        }
        viewer.selectImage(index);
    };
    carouselManger.contentDivResized=function(){
        var width = this.contentDiv.innerWidth();
        var contentDivLi=this.contentDivLis[0];
        if (contentDivLi.initialized!==undefined){
            contentDivLi.viewer.resize(width, width/500*333);
        }
    };
    carouselManger.initContentDivLi=function(){
        this.contentDivLis = this.contentDiv.find('li');
    }
    return carouselManger;
};

function VideoCarouselManger(container, data){
    var carouselManger = new CarouselManger(container, data);
    carouselManger.contentItemUnFocus=function(index){
        if (index>-1){
            this.contentDivLis[index].player.stopVideo();
        }
    };
    carouselManger.contentItemFocus=function(index){
        var contentDivLi=this.contentDivLis[index];
        if (contentDivLi.initialized===undefined){
            var div=$(contentDivLi).find('div');
            var divId='player'+index;
            div.attr('id', divId);
            var rowData=this.data[index];
            var player = new YT.Player(divId, {
                height: this.contentDiv.innerWidth()/500*333,
                width: this.contentDiv.innerWidth(),
                videoId: rowData[0]
            });
            contentDivLi.player=player;
            $(contentDivLi).find('p').text(rowData[2]);
            contentDivLi.initialized=true;
        };
        this.contentDiv.jcarousel('scroll', index, true);
    };
    carouselManger.contentDivResized=function(){
        var width = this.contentDiv.innerWidth();
        $.each(this.contentDivLis, function(index, contentDivLi){
            if (contentDivLi.initialized!==undefined){
                contentDivLi.player.setSize(width, width/500*333);
            }
        });
    };
    return carouselManger;
}