package com.example.project;

import java.util.ArrayList;
import java.util.List;
public class QuizRepo {

    public static List<QuizActivity.Question> getBasicQuestions() {
        List<QuizActivity.Question> questions = new ArrayList<>();
        questions.add(new QuizActivity.Question("หน่วยประมวลผลกลางของคอมพิวเตอร์คืออะไร",
                new String[]{"ALU", "Cache", "CPU", "GPU"}, 2));
        questions.add(new QuizActivity.Question("RAM จัดเป็นหน่วยความจำแบบใด",
                new String[]{"แบบชั่วคราว/ลบเมื่อปิดเครื่อง", "แบบถาวร", "แบบนำออกได้เท่านั้น", "แบบสำรองเท่านั้น"}, 0));
        questions.add(new QuizActivity.Question("โปรโตคอล TCP/IP ใช้สำหรับอะไร",
                new String[]{"เข้ารหัสไฟล์", "สื่อสารเครือข่ายอินเทอร์เน็ต", "ควบคุมการแสดงผลจอภาพ", "จัดการไฟล์ระบบ"}, 1));
        questions.add(new QuizActivity.Question("ภาษาใดจัดเป็นระดับต่ำสุดในตัวเลือกนี้",
                new String[]{"Java", "Python", "C", "SQL"}, 2));
        questions.add(new QuizActivity.Question("ระบบปฏิบัติการทำหน้าที่หลักใด",
                new String[]{"จัดการทรัพยากรฮาร์ดแวร์/กระบวนการ", "แก้ไวยากรณ์ของโปรแกรม", "ส่งอีเมล", "เขียนเว็บเพจอัตโนมัติ"}, 0));
        questions.add(new QuizActivity.Question("ไมโครคอนโทรลเลอร์เหมาะกับงานแบบใด",
                new String[]{"ควบคุมอุปกรณ์/เซนเซอร์ขนาดเล็ก", "เรนเดอร์กราฟิก 3D หนักๆ", "โฮสต์เว็บขนาดใหญ่", "ฝึกโมเดลขนาดใหญ่"}, 0));
        questions.add(new QuizActivity.Question("Git ใช้เพื่ออะไร",
                new String[]{"ควบคุมเวอร์ชันซอร์สโค้ด", "สร้างไบนารี", "เขียนเอกสาร PDF", "ทดสอบหน่วยความจำ"}, 0));
        questions.add(new QuizActivity.Question("การดีบักควรทำอย่างไรก่อน",
                new String[]{"ทำซ้ำปัญหาให้ได้/ระบุเงื่อนไขเกิด", "แก้สุ่มไปก่อน", "เปลี่ยนเครื่องคอมพิวเตอร์", "ลบโค้ดทั้งหมด"}, 0));
        questions.add(new QuizActivity.Question("วงจรไฟฟ้ากระแสตรง หน่วยแรงดันคือ",
                new String[]{"A", "V", "Ω", "Hz"}, 1));
        questions.add(new QuizActivity.Question("IoT ประกอบด้วยแนวคิดใด",
                new String[]{"อุปกรณ์เชื่อมต่อเครือข่าย ส่งข้อมูลสู่คลาวด์/แอป", "อุปกรณ์ทำงานออฟไลน์เท่านั้น", "เครือข่ายเฉพาะในห้องแลป", "ใช้ได้เฉพาะคอมพิวเตอร์ตั้งโต๊ะ"}, 0));
        return questions;
    }

    public static List<QuizActivity.Question> getMajorQuestions() {
        List<QuizActivity.Question> questions = new ArrayList<>();

        // RA
        questions.add(new QuizActivity.Question("ส่วนใดมักใช้ควบคุมมอเตอร์ในหุ่นยนต์",
                new String[]{"LDR", "H-bridge / Motor driver", "Heat sink", "Optocoupler"}, 1, "RA"));
        questions.add(new QuizActivity.Question("ROS ใช้หลัก ๆ เพื่อ",
                new String[]{"ดีไซน์ UI", "สื่อสาร/ประสานงานระหว่าง node ของหุ่นยนต์", "เก็บข้อมูล Big Data", "ทดสอบเว็บ"}, 1, "RA"));
        questions.add(new QuizActivity.Question("เซนเซอร์วัดระยะยอดนิยม",
                new String[]{"DHT11", "Ultrasonic / LiDAR", "RTC", "XBee"}, 1, "RA"));
        questions.add(new QuizActivity.Question("คำว่า Kinematics เกี่ยวข้องกับ",
                new String[]{"การเคลื่อนที่/ตำแหน่งแขนกล", "ไฟฟ้ากำลัง", "สถาปัตยกรรมซอฟต์แวร์", "ความถี่สัญญาณนาฬิกา"}, 0, "RA"));
        questions.add(new QuizActivity.Question("PLC มักใช้ในงาน",
                new String[]{"คลาวด์คอมพิวติ้ง", "เกมมือถือ", "อุตสาหกรรม/สายการผลิต", "สื่อสังคมออนไลน์"}, 2, "RA"));

        // ASE
        questions.add(new QuizActivity.Question("Design Pattern ที่ใช้สร้างออบเจ็กต์อย่างยืดหยุ่น",
                new String[]{"Strategy", "Factory", "Observer", "Adapter"}, 1, "ASE"));
        questions.add(new QuizActivity.Question("CI/CD ช่วยเรื่อง",
                new String[]{"ตกแต่ง UI", "ทดสอบ/ดีพลอยอัตโนมัติอย่างต่อเนื่อง", "เขียนเอกสาร", "จัดการบัญชีผู้ใช้"}, 1, "ASE"));
        questions.add(new QuizActivity.Question("การทดสอบระดับหน่วยคือ",
                new String[]{"ทดสอบฟังก์ชัน/คลาสเฉพาะจุด", "ทดสอบระบบปลายทาง", "ทดสอบประสิทธิภาพเครือข่าย", "ทดสอบความปลอดภัย"}, 0, "ASE"));
        questions.add(new QuizActivity.Question("แนวคิด Clean Architecture เน้น",
                new String[]{"แยกเลเยอร์/ลดการพึ่งพา/ทดสอบง่าย", "ใช้ฐานข้อมูลเดียว", "ใช้ UI เดียว", "เขียนภาษาเดียว"}, 0, "ASE"));
        questions.add(new QuizActivity.Question("การทำงานพร้อมกันควรระวัง",
                new String[]{"Race condition / Deadlock", "สีปุ่ม", "ขนาดโลโก้", "ชื่อคลาสยาวไป"}, 0, "ASE"));

        // CYB
        questions.add(new QuizActivity.Question("หลักการความปลอดภัย CIA หมายถึง",
                new String[]{"Confidentiality, Integrity, Availability", "Control, Inspect, Alert", "Code, Inject, Attack", "Central, Internal, Admin"}, 0, "CYB"));
        questions.add(new QuizActivity.Question("OWASP Top 10 เกี่ยวกับ",
                new String[]{"ความเสี่ยงแอปเว็บที่พบบ่อย", "อัลกอริทึม ML", "มาตรฐานฮาร์ดแวร์", "โปรโตคอลสื่อสาร"}, 0, "CYB"));
        questions.add(new QuizActivity.Question("เครื่องมือดักจับแพ็กเก็ต",
                new String[]{"Jenkins", "Docker", "Wireshark", "Postman"}, 2, "CYB"));
        questions.add(new QuizActivity.Question("การเข้ารหัสแบบสมมาตร",
                new String[]{"คีย์เดียวสำหรับเข้ารหัส/ถอดรหัส", "ใช้คีย์คู่", "ไม่ใช้คีย์", "ใช้รหัสผ่านอย่างเดียว"}, 0, "CYB"));
        questions.add(new QuizActivity.Question("Incident Response ขั้นแรกมักเป็น",
                new String[]{"Identify/Contain", "Public Post", "ลบระบบทิ้งทันที", "เปลี่ยนทีม"}, 0, "CYB"));
        return questions;
    }

    public static List<QuizActivity.Question> getOthersQuestions() {
        List<QuizActivity.Question> questions = new ArrayList<>();
        // CS
        questions.add(new QuizActivity.Question("โครงสร้างข้อมูลที่เข้าถึงท้าย/ต้นเร็วและเรียงเชื่อมคือ",
                new String[]{"Linked List", "Array", "HashMap", "Stack"}, 0, "CS"));
        questions.add(new QuizActivity.Question("Big-O ของ Binary Search",
                new String[]{"O(n)", "O(log n)", "O(1)", "O(n log n)"}, 1, "CS"));
        questions.add(new QuizActivity.Question("DFA/NFA อยู่ในหัวข้อ",
                new String[]{"Automata/Formal Language", "Cryptography", "Graphics", "OS"}, 0, "CS"));
        questions.add(new QuizActivity.Question("Sorting แบบแบ่งแล้วรวม",
                new String[]{"Merge Sort", "Insertion", "Selection", "Bubble"}, 0, "CS"));
        questions.add(new QuizActivity.Question("Stack/Heap เป็นเรื่องของ",
                new String[]{"Program memory model", "UI", "Network", "File system"}, 0, "CS"));

        // SE
        questions.add(new QuizActivity.Question("เอกสารเก็บความต้องการคือ",
                new String[]{"SRS", "ERD", "BOM", "SDK"}, 0, "SE"));
        questions.add(new QuizActivity.Question("Agile เน้น",
                new String[]{"ส่งมอบเป็นช่วง ๆ / รับฟีดแบ็กเร็ว", "สเปคตายตัว", "ปล่อยปีละครั้ง", "ไม่มีรีวิว"}, 0, "SE"));
        questions.add(new QuizActivity.Question("Code Review ช่วย",
                new String[]{"คุณภาพ/ความปลอดภัย/ความรู้ร่วม", "เพิ่มบั๊ก", "ลดทีมเวิร์ก", "ช้าลงเสมอ"}, 0, "SE"));
        questions.add(new QuizActivity.Question("Unit test ต่างกับ Integration test ที่",
                new String[]{"ขอบเขตเล็ก/ไม่แตะระบบจริง", "ใช้ผู้ใช้จริง", "ทดสอบโหลด", "ไม่ต้องเขียนโค้ด"}, 0, "SE"));
        questions.add(new QuizActivity.Question("Issue Tracking ใช้เครื่องมือ",
                new String[]{"Jira / GitHub Issues", "Photoshop", "Figma", "Slack อย่างเดียว"}, 0, "SE"));

        // IT
        questions.add(new QuizActivity.Question("อุปกรณ์แจก IP ภายในเครือข่าย",
                new String[]{"DHCP Server", "DNS", "NTP", "IDS"}, 0, "IT"));
        questions.add(new QuizActivity.Question("โดเมน/กลุ่มนโยบายในองค์กร",
                new String[]{"Active Directory", "FTP", "CDN", "VPN Client"}, 0, "IT"));
        questions.add(new QuizActivity.Question("2FA ทำเพื่อ",
                new String[]{"เพิ่มความปลอดภัยการยืนยันตัวตน", "เร่งอินเทอร์เน็ต", "สำรองไฟล์", "ปิดโฆษณา"}, 0, "IT"));
        questions.add(new QuizActivity.Question("สำรองข้อมูลแบบจุดเวลา",
                new String[]{"Snapshot/Incremental Backup", "CRC", "NAT", "Telnet"}, 0, "IT"));
        questions.add(new QuizActivity.Question("ITIL เกี่ยวกับ",
                new String[]{"IT Service Management", "Game Design", "PCB Layout", "Shader"}, 0, "IT"));

        // DS
        questions.add(new QuizActivity.Question("ค่ากลางที่ไม่ไวต่อ outlier",
                new String[]{"Mean", "Median", "Mode", "Variance"}, 1, "DS"));
        questions.add(new QuizActivity.Question("ไลบรารี DataFrame ใน Python",
                new String[]{"Pandas", "TensorFlow", "Flask", "OpenCV"}, 0, "DS"));
        questions.add(new QuizActivity.Question("แผนภาพความหนาแน่นต่อเนื่อง",
                new String[]{"KDE / Density Plot", "Bar Chart", "Pie", "Box"}, 0, "DS"));
        questions.add(new QuizActivity.Question("Train/Validation/Test แยกเพื่อ",
                new String[]{"ประเมิน/กัน overfitting", "เร็วขึ้นเสมอ", "ใช้ GPU", "แก้ scalability"}, 0, "DS"));
        questions.add(new QuizActivity.Question("A/B Test ต้องมี",
                new String[]{"กลุ่มควบคุม, สุ่ม, เมตริก", "แค่กราฟสวย", "ทีมตลาดอย่างเดียว", "ไม่มีสถิติ"}, 0, "DS"));

        return questions;
    }
}