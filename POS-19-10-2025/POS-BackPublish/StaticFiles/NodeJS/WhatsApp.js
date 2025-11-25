const { Client, LocalAuth } = require('whatsapp-web.js');
//const args = process.argv.slice(2);
//const jsonString = args[0];
let messagesData = [];
let isQrCode;
//if (jsonString && jsonString.length) messagesData = JSON.parse(JSON.stringify(jsonString));

module.exports = async function (callback, input) {
    messagesData = input.messages;
    isQrCode = input.isQrCode;
    let res = await runAsyncTask();
    callback(null, res);
    //let res = {
    //    IsQrCode: false,
    //    Data: input
    //}
    //callback(null, res);
}
async function runAsyncTask() {

    const result = await whatsAppHandler();
    return result;
}
function whatsAppHandler() {
    return new Promise((resolve, reject) => {
        const client = new Client({ authStrategy: new LocalAuth() });
        client.on('qr', (qrCode) => {
            // if need to reed qr from user so remove all messageData
            if (messagesData && messagesData.length) messagesData = null;
            let model = {
                IsQrCode: isQrCode,
                Data: qrCode.toString()
            }
            resolve(model);

        });

        client.on('ready', () => {

            if (isQrCode) {
                let model = {
                    IsQrCode: isQrCode,
                    Data: messagesData
                }
                resolve(model);
            }


            for (const messageData of messagesData) {
                let number = messageData.number;

                number = number.includes('@c.us') ? number : `${number}@c.us`;

                const finalNumber = number.toString().replace(/[- )(]/g, "").replace('+', '');

                client.sendMessage(finalNumber, messageData.message);
            }

           
            let model = {
                IsQrCode: isQrCode,
                Data: true
            }
            resolve(model);
        });

        client.initialize();

    });
}
