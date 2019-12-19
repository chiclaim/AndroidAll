/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * When the window loads, connect the showNotificationBtn to the Javascript
 * interface "window.NotificationBind"
 */
window.onload = function() {
    console.log('window.onload');
    var loadFileBtn = document.querySelector('.loadFileBtn');
    var fileInputField = document.querySelector('.hiddenFileInput');
    var img = document.querySelector('img');

    loadFileBtn.addEventListener('click', function() {
        fileInputField.click();
    }.bind(this));
    fileInputField.addEventListener('change', function(evt) {
        console.log('Change', evt);
        var reader = new FileReader();
        reader.onload = function (evt) {
            img.src = evt.target.result;
        };

        reader.readAsDataURL(evt.target.files[0]);
    });
};