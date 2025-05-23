import MailTransporter from "./MailTransporter";

const mailTransporter = new MailTransporter
const transporter = await mailTransporter.createTestAccount();

export default class MailService {
    async userCreatedNewAccount(userName: string, email: string) {
        const info = await transporter.sendMail(
            {
                from: '"SoonBward" <no-reply@soonbward.com>',
                to: email,
                subject: `Bienvenue sur SoonBward ${userName} !`,
                html: `
                <html>
                    <head>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                            }

                            .header {
                                background-color: #f2f4f8;
                                padding: 10px;
                                text-align: center;
                            }

                            .content {
                                margin: 20px;
                                padding: 20px;
                                background-color: #fff;
                                border-radius: 5px;
                                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                            }

                            .footer {
                                text-align: center;
                                padding: 10px;
                                font-size: 12px;
                                color: #777;
                            }
                        </style>
                    </head>

                    <body>
                        <div class="header">
                            <h1>Bienvenue sur SoonBward, ${userName}!</h1>
                        </div>
                        <div class="content">
                            <p>
                                Nous sommes ravis que vous ayez décidé de nous rejoindre.<br> SoonBward est conçu pour vous offrir la meilleure expérience possible.
                            </p>
                            <br>
                            <p>Veuillez confirmer votre adresse email en cliquant sur le lien ci-dessous :</p>
                            <p><a href="https://soonbward.com/verify?email=${encodeURIComponent(email)}"
                                    style="color: #1a0dab; text-decoration: none;">Confirmer mon adresse email</a></p>
                        </div>
                        <div class="footer">
                            Merci d'avoir rejoint SoonBward! Pour toute question, n'hésitez pas à nous contacter à <a
                                href="mailto:support@soonbward.com">support@soonbward.com</a>.
                        </div>
                    </body>
                </html>
            `
            }
        );

        return info;
    }
}