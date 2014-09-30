var clearError = function(formGroup){
            formGroup.removeClass('has-error');
            formGroup.find('span[errorCode*=e]').addClass('hidden');
        }

        var setError = function(formGroup, errorCode){
            formGroup.addClass('has-error');
            formGroup.find('span[errorCode='+errorCode+']').removeClass('hidden');
        }

        var clearErrors = function(form){
            $.each(form.find('.form-group'), function(index, value){
                clearError($(value));
            });
        };

var validateNotNull = function(formGroup){
            if (formGroup.find('input.form-control').val()===''){
                setError(formGroup, 'e1');
                return false;
            }
            return true;
        };

        var validateEqual = function(formGroup, value){
            if (formGroup.find('input.form-control').val()!==value){
                setError(formGroup, 'e1');
                return false;
            }
            return true;
        };

        var validateEmail = function(formGroup){
            var email = formGroup.find('input.form-control').val();
            if (email===''){
                setError(formGroup, 'e1');
                return false;
            }
            var atpos = email.indexOf('@');
            var dotpos = email.lastIndexOf('.');
            if (atpos< 1 || dotpos<atpos+2 || dotpos+2>=email.length) {
                setError(formGroup, 'e2');
                return false;
            }
            return true;
        }

        var validatePassword = function(formGroup){
            if (formGroup.find('input.form-control').val().length<5){
                setError(formGroup, 'e1');
                return false;
            }
            return true;
        }