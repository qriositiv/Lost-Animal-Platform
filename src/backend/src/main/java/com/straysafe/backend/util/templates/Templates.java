package com.straysafe.backend.util.templates;

public class Templates {
    // PLACEHOLDERS BY SPOT:
    // 1st : Pet type ,
    // 2nd : Image base64,
    // 3rd : Pet name,
    // 4th : Pet breed,
    // 5th : Lost since,
    // 6th : Last seen,
    // 7th : notes,
    // 8th : phone number

    public static String REPORT_SUBMISSION_SUBJECT = "%s your report has been submitted successfully!";
    public static String LOST_POSTER_HTML_TEMPLATE = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <title>Missing Pet Poster</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                    }
                    .poster {
                        border: 2px solid black;
                        padding: 20px;
                        max-width: 600px;
                        margin: auto;
                        text-align: center;
                    }
                    .header {
                        background-color: black;
                        color: white;
                        padding: 5px;
                        font-size: 24px;
                        font-weight: bold;
                    }
                    .content {
                        margin: 10px 0;
                    }
                    .details {
                        text-align: left;
                        font-size: 18px;
                    }
                    .details p {
                        margin: 10px 0;
                    }
                    .contact {
                        background-color: black;
                        color: white;
                        padding: 10px;
                        font-size: 20px;
                        font-weight: bold;
                    }
                </style>
            </head>
            <body>
            
            <div class="poster">
                <div class="header">MISSING %s</div>
                <div class="content">
                    <div class="dog-image">
                        <img style="width: auto; height: 500px;" src="%s" alt="Missing pet" />
                    </div>
                    <div class="details">
                        <p style="font-size: 30px; font-weight: bold; padding-bottom: 0px; margin-bottom: 0px;">%s</p>
                        <p style="font-size: 28px; padding-top: 0px; margin-top: 0px;">%s</p>
                        <hr />
                        <p><strong>Lost Since</strong><br />%s</p>
                        <p><strong>Last Seen At</strong><br />%s</p>
                        <p><strong>Notes</strong><br />%s</p>
                    </div>
                </div>
                <div class="contact">
                    CALL OR TEXT WITH ANY INFORMATION<br />
                    <span style="font-size: 28px;">%s</span>
                </div>
            </div>
            
            </body>
            </html>   
        """;

    // 1st and 2nd Name and Surname
    // 3rd Report ID
    // 4th Submission date
    // 5th Location address
    public static String FOUND_AND_SEEN_EMAIL = """
            Dear %s %s,
            
            Thank you for submitting your report regarding a %s pet. We appreciate your effort to help reunite lost pets with their owners.
            
            Here are the details of your submission:
            
            Report ID: %s
            Date Submitted: %s
            Last Seen Location: %s
            
            Your report will be shared with the community to increase the chances of finding the pet's owner.
            
            In the meantime, if you have any additional information or updates, please feel free to contact us at help.straysafe@gmail.com.
            
            Thank you once again for your kindness and assistance.
            
            Best regards,
            Team StraySafe
            """;

    // Username
    public static String REGISTER_WELCOME_SUBJECT = "Welcome to StraySafe %s";

    // Name Surname 1st and 2nd
    public static String REGISTER_WELCOME_EMAIL = """
            Dear %s %s,
            
            Welcome to StraySafe!
            
            We are excited to have you join our community. At StraySafe, we are dedicated to help pet owners and pet supporters.
            
            If you have any questions, feel free to contact us at help.straysafe@gmail.com , we're here to help!
            
            Best regards,
            The StraySafe Team
            """;

}