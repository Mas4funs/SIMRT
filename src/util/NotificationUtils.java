/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author nursalim
 */
public class NotificationUtils {
    
    
    String from = "klp2pemvis@gmail.com";
    String host = "smtp.gmail.com";
    
    public void sentLaporanWarga(Map data){
        
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass 
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("klp2pemvis@gmail.com", "znnrzgmvkfyzullb");
            }

        });
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress((String)data.get("to")));

            // Set Subject: header field
            message.setSubject("Laporan Warga "+data.get("nolaporan").toString());

            Multipart multipart = new MimeMultipart();

            MimeBodyPart attachmentPart = new MimeBodyPart();

            MimeBodyPart textPart = new MimeBodyPart();

            try {
            File f =new File("C:/tmp/pdf/rpt_laporan_warga.pdf");

                attachmentPart.attachFile(f);

                
                StringBuffer sb = new StringBuffer();
                sb.append("Laporan Warga\n\n");
                sb.append("Kepada Warga RT 02 / 11\n");
                sb.append("Perumahan Legok Permai Cluster Heliconia Tangerang.\n");
                sb.append("Berikut terlampir atas \"Laporan Warga\" yang diajukan atas nama :\n\n");
                sb.append("ID Warga\t\t: "+data.get("idwarga")+"\n");
                sb.append("Nama\t\t\t : "+data.get("nama")+"\n");
                sb.append("Jenis Laporan\t     : "+data.get("jenislaporan")+"\n\n\n\n\n");
                sb.append("Ketua RT\n");
                sb.append("Zulrachman\n");
                
                textPart.setText(sb.toString());
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);

            } catch (IOException e) {

                e.printStackTrace();

            }

            message.setContent(multipart);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    public void sentSuratWarga(Map data){
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass 
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("klp2pemvis@gmail.com", "znnrzgmvkfyzullb");
            }

        });
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress((String)data.get("to")));

            // Set Subject: header field
            message.setSubject("Surat Warga "+data.get("nosurat").toString());

            Multipart multipart = new MimeMultipart();

            MimeBodyPart attachmentPart = new MimeBodyPart();

            MimeBodyPart textPart = new MimeBodyPart();

            try {

               File f =new File("C:/tmp/pdf/rpt_surat_warga.pdf");

                attachmentPart.attachFile(f);
                
                StringBuffer sb = new StringBuffer();
                sb.append("Surat Warga\n\n");
                sb.append("Kepada Warga RT 02 / 11\n");
                sb.append("Perumahan Legok Permai Cluster Heliconia Tangerang.\n");
                sb.append("Berikut terlampir atas \"Surat Warga\" yang diajukan atas nama :\n\n");
                sb.append("ID Warga\t\t: "+data.get("idwarga")+"\n");
                sb.append("Nama\t\t\t : "+data.get("nama")+"\n");
                sb.append("Jenis Surat\t\t: "+data.get("jenissurat")+"\n\n\n\n\n");
                sb.append("Ketua RT\n");
                sb.append("Zulrachman\n");
                
                textPart.setText(sb.toString());
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);

            } catch (IOException e) {

                e.printStackTrace();

            }

            message.setContent(multipart);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    public void sentEmailWarga(Map data){
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass 
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("klp2pemvis@gmail.com", "znnrzgmvkfyzullb");
            }

        });
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress((String)data.get("to")));

            // Set Subject: header field
            message.setSubject("Email Warga "+data.get("noemail").toString());

            Multipart multipart = new MimeMultipart();

            MimeBodyPart attachmentPart = new MimeBodyPart();

            MimeBodyPart textPart = new MimeBodyPart();

            try {

               File f =new File("C:/tmp/pdf/rpt_email_warga.pdf");

                attachmentPart.attachFile(f);
                
                StringBuffer sb = new StringBuffer();
                sb.append("Email Warga\n\n");
                sb.append("Kepada Warga RT 02 / 11\n");
                sb.append("Perumahan Legok Permai Cluster Heliconia Tangerang.\n");
                sb.append("Berikut terlampir atas \"Email Warga\" yang diajukan atas nama :\n\n");
                sb.append("ID Warga\t\t  : "+data.get("idwarga")+"\n");
                sb.append("Nama\t\t\t   : "+data.get("nama")+"\n");
                sb.append("No Rumah\t\t: "+data.get("norumah")+"\n");
                sb.append("Nama Iuran\t\t: "+data.get("namaiuran")+"\n");
                sb.append("Jenis Iuran\t\t  :"+data.get("jenisiuran")+"\n\n\n\n\n");
                sb.append("Ketua RT\n");
                sb.append("Zulrachman\n");
                
                textPart.setText(sb.toString());
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);

            } catch (IOException e) {

                e.printStackTrace();

            }

            message.setContent(multipart);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    public void sentIuranWarga(Map data){
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass 
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("klp2pemvis@gmail.com", "znnrzgmvkfyzullb");
            }

        });
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress((String)data.get("to")));

            // Set Subject: header field
            message.setSubject("Iuran Warga "+data.get("noiuran").toString());

            Multipart multipart = new MimeMultipart();

            MimeBodyPart attachmentPart = new MimeBodyPart();

            MimeBodyPart textPart = new MimeBodyPart();

            try {

               File f =new File("C:/tmp/pdf/rpt_iuran_warga.pdf");

                attachmentPart.attachFile(f);
                
                StringBuffer sb = new StringBuffer();
                sb.append("LIuran Warga\n\n");
                sb.append("Kepada Warga RT 02 / 11\n");
                sb.append("Perumahan Legok Permai Cluster Heliconia Tangerang.\n");
                sb.append("Berikut terlampir atas \"Iuran Warga\" yang diajukan atas nama :\n\n");
                sb.append("ID Warga\t\t  : "+data.get("idwarga")+"\n");
                sb.append("No Rumah\t\t: "+data.get("norumah")+"\n");
                sb.append("Nomor Email\t\t: "+data.get("noemail")+"\n");
                sb.append("Jumlah Bayar\t:\t: "+data.get("jmlpembayaran")+"\n");
                sb.append("Status Iuran\t\t   : "+data.get("statusiuran")+"\n\n\n\n\n");
                sb.append("Ketua RT\n");
                sb.append("Zulrachman\n");
                
                textPart.setText(sb.toString());
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);

            } catch (IOException e) {

                e.printStackTrace();

            }

            message.setContent(multipart);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        NotificationUtils utils = new NotificationUtils();
        
        Map data = new HashMap();
        data.put("to", "virgan1st@gmail.com");
        data.put("noPemesanan", "P210720RW1Y");
        data.put("tglPemesanan", "20/07/2021");
        data.put("nama", "Nursalim");
        data.put("noKTP", "332911111111");
        data.put("statusPembayaran", "Lunas");
        data.put("namaPaket", "Paket Umrah 1");
        
        //utils.sentEmailPemesanan(data);
        utils.sentEmailWarga(data);
    }
}
