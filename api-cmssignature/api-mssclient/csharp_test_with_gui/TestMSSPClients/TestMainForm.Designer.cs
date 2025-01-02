namespace TestMSSPClients
{
    partial class TestMainForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.cBoxOperator = new System.Windows.Forms.ComboBox();
            this.label1 = new System.Windows.Forms.Label();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.label7 = new System.Windows.Forms.Label();
            this.txtMSSPTimeout = new System.Windows.Forms.TextBox();
            this.label6 = new System.Windows.Forms.Label();
            this.txtConnectionTimeout = new System.Windows.Forms.TextBox();
            this.label4 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.txtMsspDomain = new System.Windows.Forms.TextBox();
            this.txtMsspUserPwd = new System.Windows.Forms.TextBox();
            this.txtMsspUserName = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.txtMobileNumber = new System.Windows.Forms.TextBox();
            this.label5 = new System.Windows.Forms.Label();
            this.btnTestProfileService = new System.Windows.Forms.Button();
            this.btnTestSignHash = new System.Windows.Forms.Button();
            this.groupBox3 = new System.Windows.Forms.GroupBox();
            this.btnSignFile = new System.Windows.Forms.Button();
            this.btnBrowseFile = new System.Windows.Forms.Button();
            this.txtFileForSign = new System.Windows.Forms.TextBox();
            this.label8 = new System.Windows.Forms.Label();
            this.groupBox1.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.groupBox3.SuspendLayout();
            this.SuspendLayout();
            // 
            // cBoxOperator
            // 
            this.cBoxOperator.FormattingEnabled = true;
            this.cBoxOperator.Items.AddRange(new object[] {
            "Turkcell",
            "TurkTelekom"});
            this.cBoxOperator.Location = new System.Drawing.Point(82, 12);
            this.cBoxOperator.Name = "cBoxOperator";
            this.cBoxOperator.Size = new System.Drawing.Size(121, 21);
            this.cBoxOperator.TabIndex = 0;
            this.cBoxOperator.SelectedIndexChanged += new System.EventHandler(this.cBoxOperator_SelectedIndexChanged);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(162)));
            this.label1.Location = new System.Drawing.Point(12, 12);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(64, 13);
            this.label1.TabIndex = 1;
            this.label1.Text = "Operatör :";
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.label7);
            this.groupBox1.Controls.Add(this.txtMSSPTimeout);
            this.groupBox1.Controls.Add(this.label6);
            this.groupBox1.Controls.Add(this.txtConnectionTimeout);
            this.groupBox1.Controls.Add(this.label4);
            this.groupBox1.Controls.Add(this.label3);
            this.groupBox1.Controls.Add(this.txtMsspDomain);
            this.groupBox1.Controls.Add(this.txtMsspUserPwd);
            this.groupBox1.Controls.Add(this.txtMsspUserName);
            this.groupBox1.Controls.Add(this.label2);
            this.groupBox1.Location = new System.Drawing.Point(272, 11);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(393, 175);
            this.groupBox1.TabIndex = 2;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Mssp Kullanıcı Bilgileri";
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(6, 137);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(127, 13);
            this.label7.TabIndex = 9;
            this.label7.Text = "MSSP Request Timeout :";
            // 
            // txtMSSPTimeout
            // 
            this.txtMSSPTimeout.Location = new System.Drawing.Point(142, 134);
            this.txtMSSPTimeout.Name = "txtMSSPTimeout";
            this.txtMSSPTimeout.Size = new System.Drawing.Size(221, 20);
            this.txtMSSPTimeout.TabIndex = 8;
            this.txtMSSPTimeout.Text = "180";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(6, 111);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(108, 13);
            this.label6.TabIndex = 7;
            this.label6.Text = "Connection Timeout :";
            // 
            // txtConnectionTimeout
            // 
            this.txtConnectionTimeout.Location = new System.Drawing.Point(142, 108);
            this.txtConnectionTimeout.Name = "txtConnectionTimeout";
            this.txtConnectionTimeout.Size = new System.Drawing.Size(221, 20);
            this.txtConnectionTimeout.TabIndex = 6;
            this.txtConnectionTimeout.Text = "-1";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(65, 85);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(49, 13);
            this.label4.TabIndex = 5;
            this.label4.Text = "Domain :";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(65, 55);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(43, 13);
            this.label3.TabIndex = 4;
            this.label3.Text = "Parola :";
            // 
            // txtMsspDomain
            // 
            this.txtMsspDomain.Location = new System.Drawing.Point(142, 78);
            this.txtMsspDomain.Name = "txtMsspDomain";
            this.txtMsspDomain.Size = new System.Drawing.Size(221, 20);
            this.txtMsspDomain.TabIndex = 3;
            // 
            // txtMsspUserPwd
            // 
            this.txtMsspUserPwd.Location = new System.Drawing.Point(142, 52);
            this.txtMsspUserPwd.Name = "txtMsspUserPwd";
            this.txtMsspUserPwd.Size = new System.Drawing.Size(221, 20);
            this.txtMsspUserPwd.TabIndex = 2;
            // 
            // txtMsspUserName
            // 
            this.txtMsspUserName.Location = new System.Drawing.Point(142, 22);
            this.txtMsspUserName.Name = "txtMsspUserName";
            this.txtMsspUserName.Size = new System.Drawing.Size(221, 20);
            this.txtMsspUserName.TabIndex = 1;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(38, 29);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(70, 13);
            this.label2.TabIndex = 0;
            this.label2.Text = "Kullanıcı Adı :";
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.txtMobileNumber);
            this.groupBox2.Controls.Add(this.label5);
            this.groupBox2.Location = new System.Drawing.Point(12, 41);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(204, 100);
            this.groupBox2.TabIndex = 3;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Mobil Kullanıcı Bilgileri";
            // 
            // txtMobileNumber
            // 
            this.txtMobileNumber.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(162)));
            this.txtMobileNumber.Location = new System.Drawing.Point(28, 56);
            this.txtMobileNumber.Name = "txtMobileNumber";
            this.txtMobileNumber.RightToLeft = System.Windows.Forms.RightToLeft.Yes;
            this.txtMobileNumber.Size = new System.Drawing.Size(126, 26);
            this.txtMobileNumber.TabIndex = 1;
            this.txtMobileNumber.Text = "05319321172";
            this.txtMobileNumber.TextChanged += new System.EventHandler(this.txtMobileNumber_TextChanged);
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(38, 30);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(96, 13);
            this.label5.TabIndex = 0;
            this.label5.Text = "Telefon Numarası :";
            // 
            // btnTestProfileService
            // 
            this.btnTestProfileService.Location = new System.Drawing.Point(15, 218);
            this.btnTestProfileService.Name = "btnTestProfileService";
            this.btnTestProfileService.Size = new System.Drawing.Size(155, 29);
            this.btnTestProfileService.TabIndex = 4;
            this.btnTestProfileService.Text = "Test Profile Service";
            this.btnTestProfileService.UseVisualStyleBackColor = true;
            this.btnTestProfileService.Click += new System.EventHandler(this.btnTestProfileService_Click);
            // 
            // btnTestSignHash
            // 
            this.btnTestSignHash.Location = new System.Drawing.Point(185, 218);
            this.btnTestSignHash.Name = "btnTestSignHash";
            this.btnTestSignHash.Size = new System.Drawing.Size(155, 30);
            this.btnTestSignHash.TabIndex = 5;
            this.btnTestSignHash.Text = "Test Sign Hash";
            this.btnTestSignHash.UseVisualStyleBackColor = true;
            this.btnTestSignHash.Click += new System.EventHandler(this.btnTestSignHash_Click);
            // 
            // groupBox3
            // 
            this.groupBox3.Controls.Add(this.btnSignFile);
            this.groupBox3.Controls.Add(this.btnBrowseFile);
            this.groupBox3.Controls.Add(this.txtFileForSign);
            this.groupBox3.Controls.Add(this.label8);
            this.groupBox3.Location = new System.Drawing.Point(12, 266);
            this.groupBox3.Name = "groupBox3";
            this.groupBox3.Size = new System.Drawing.Size(449, 158);
            this.groupBox3.TabIndex = 6;
            this.groupBox3.TabStop = false;
            this.groupBox3.Text = "Dosya İmzalama";
            // 
            // btnSignFile
            // 
            this.btnSignFile.Location = new System.Drawing.Point(143, 111);
            this.btnSignFile.Name = "btnSignFile";
            this.btnSignFile.Size = new System.Drawing.Size(155, 30);
            this.btnSignFile.TabIndex = 6;
            this.btnSignFile.Text = "Test Sign File";
            this.btnSignFile.UseVisualStyleBackColor = true;
            this.btnSignFile.Click += new System.EventHandler(this.btnSignFile_Click);
            // 
            // btnBrowseFile
            // 
            this.btnBrowseFile.Location = new System.Drawing.Point(402, 23);
            this.btnBrowseFile.Name = "btnBrowseFile";
            this.btnBrowseFile.Size = new System.Drawing.Size(41, 20);
            this.btnBrowseFile.TabIndex = 4;
            this.btnBrowseFile.Text = "...";
            this.btnBrowseFile.UseVisualStyleBackColor = true;
            this.btnBrowseFile.Click += new System.EventHandler(this.btnBrowseFile_Click);
            // 
            // txtFileForSign
            // 
            this.txtFileForSign.Location = new System.Drawing.Point(118, 23);
            this.txtFileForSign.Multiline = true;
            this.txtFileForSign.Name = "txtFileForSign";
            this.txtFileForSign.Size = new System.Drawing.Size(275, 66);
            this.txtFileForSign.TabIndex = 3;
            this.txtFileForSign.Text = "C:\\Users\\int\\Desktop\\rmz.tmp\\API_And_Imzager_24_12_2012\\api-mssclient\\csharp_test" +
                "_with_gui\\TestMSSPClients\\TestMSSPClients\\example.txt";
            // 
            // label8
            // 
            this.label8.AutoSize = true;
            this.label8.Location = new System.Drawing.Point(12, 26);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(106, 13);
            this.label8.TabIndex = 2;
            this.label8.Text = "İmzalanacak Dosya :";
            // 
            // TestMainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(754, 467);
            this.Controls.Add(this.groupBox3);
            this.Controls.Add(this.btnTestSignHash);
            this.Controls.Add(this.btnTestProfileService);
            this.Controls.Add(this.groupBox2);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.cBoxOperator);
            this.Name = "TestMainForm";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.TestMainForm_Load);
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            this.groupBox3.ResumeLayout(false);
            this.groupBox3.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ComboBox cBoxOperator;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TextBox txtMsspDomain;
        private System.Windows.Forms.TextBox txtMsspUserPwd;
        private System.Windows.Forms.TextBox txtMsspUserName;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.TextBox txtMobileNumber;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Button btnTestProfileService;
        private System.Windows.Forms.Button btnTestSignHash;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.TextBox txtMSSPTimeout;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.TextBox txtConnectionTimeout;
        private System.Windows.Forms.GroupBox groupBox3;
        private System.Windows.Forms.Button btnBrowseFile;
        private System.Windows.Forms.TextBox txtFileForSign;
        private System.Windows.Forms.Label label8;
        private System.Windows.Forms.Button btnSignFile;
    }
}

