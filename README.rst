================================
GCM for Android Proof of Concept
================================

A simple proof of concept of an Android application that uses Google Cloud Messaging.


What does it do
---------------

The app manages the registration flow and receiving of data or notifications from GCM.

Clicking the ``Register`` button starts the registration flow with GCM and backend, when the
``registration_id`` is received from GCM it's stored and sent to backend paired with the ``device_id``.

Clicking the ``Delete Token`` button sends a DELETE call to the backend that deletes the stored ``device_id``.

When a message is received it's logged.
When a notification is received it's shown to the user; you can set the notification sound and icon
from backend, in this case we're using the default sound and the drawable ic_launcher as an icon.


How to
------

To make this app work correctly you need to create ``/app/src/main/res/values/authorization.xml``
that will contain your ``project_number`` used by the GCM.

The ``authorization.xml`` file is like this:

.. code-block:: xml

    <?xml version="1.0" encoding="utf-8"?>
    <resources>
        <string name="project_number">000000000000</string>
    </resources>


You will also need a configuration file ``google-services.json``, this file goes in your app root: ``/app/``.

`Here <https://developers.google.com/mobile/add?platform=android&cntapi=gcm&cnturl=https:%2F%2Fdevelopers.google.com%2Fcloud-messaging%2Fandroid%2Fclient&cntlbl=Continue%20Adding%20GCM%20Support&%3Fconfigured%3Dtrue/>`_
you can get your ``project_number`` and ``google-services.json``
