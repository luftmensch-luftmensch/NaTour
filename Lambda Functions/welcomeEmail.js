'use strict';

const generate_email_body = (emailBody) => `
    <html>
        <body>
            <table align="center"  cellpadding="0" cellspacing="0" width="600" >
                <tr>
                    <td bgcolor="#ffffff" style="padding: 40px 0 30px 0;"><img src="https://ibb.co/Vtbgc2V" alt="Logo"  height="230" style="display: block;"></td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff"><p style="margin: 0;">${emailBody}</p></td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" style="font-weight: 500; font-size: 11px"><p>© Copyright ${new Date().getFullYear()}. All Rights Reserved.</p></td>
                </tr>
            </table>
        </body>
    </html>
`

const sign_up_message = async(event) => {
    let email = event.request.userName;
    let code = event.request.codeParameter;
    event.response = {
        emailSubject: "Confirm your sign up",
        emailMessage: generate_email_body("<p>Your username is " + email + " and password is " + code + "</p>")
    }
    return event
}
const admin_create_user_message = async(event) => {
    let email = event.request.userName;
    let code = event.request.codeParameter;
    event.response = {
        emailSubject: "Your temporary password",
        emailMessage: generate_email_body("<p>Your username is " + email + " and password is " + code + "</p>")
    }
    return event
}
const resend_code_message = async(event) => {
    let email = event.request.userName;
    let code = event.request.codeParameter;
    event.response = {
        emailSubject: "Resend code",
        emailMessage: generate_email_body("<p>Your username is " + email + " and code is " + code + "</p>")
    }
    return event
}
const forgot_password = async(event) => {
    let email = event.request.userName;
    let code = event.request.codeParameter;
    event.response = {
        emailSubject: "Forgot password",
        emailMessage: generate_email_body("<p>Your forgot password code is " + code + "</p>")
    }
    return event
}
const update_user_attribute_message = async(event) => {
    let email = event.request.userName;
    let code = event.request.codeParameter;
    event.response = {
        emailSubject: "User updated",
        emailMessage: generate_email_body("<p>Your username is " + email + "</p>")
    }
    return event
}
const verify_user_attribute = async(event) => {
    let email = event.request.userName;
    let code = event.request.codeParameter;
    event.response = {
        emailSubject: "Verify user attribute",
        emailMessage: generate_email_body("<p>Your username is " + email + "</p>")
    }
    return event
}
const authenitcation_message = async(event) => {
    let email = event.request.userName;
    let code = event.request.codeParameter;
    event.response = {
        emailSubject: "MFA Authenitcation",
        emailMessage: generate_email_body("<p>Your username is " + email + " and code is " + code + "</p>")
    }
    return event
}
exports.handler = async(event) => {
    switch (event.triggerSource) {
        case "CustomMessage_SignUp": //Sign-up trigger whenever a new user signs him/herself up.
            return sign_up_message(event)
        case "CustomMessage_AdminCreateUser": //When the user is created with adminCreateUser() API
            return admin_create_user_message(event)
        case "CustomMessage_ResendCode": //When user requests the code again.
            return resend_code_message(event)
        case "CustomMessage_ForgotPassword": //Forgot password request initiated by user
            return forgot_password(event)
        case "CustomMessage_UpdateUserAttribute": //Whenever the user attributes are updated
            return update_user_attribute_message(event)
        case "CustomMessage_VerifyUserAttribute": //Verify mobile number/email
            return verify_user_attribute(event)
        case "CustomMessage_Authentication": //MFA authenitcation code.
            return authenitcation_message(event)
        default:
            return event

    }
};