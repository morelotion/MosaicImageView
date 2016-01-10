package cn.ffmpeg;

/**
 * 
 * @author jarlen
 * 
 */
public class NativeFFmpeg
{

	static
	{
		System.loadLibrary("ffconvert");
	}

	/**
	 * FFmpegConvertGMp4AndLogo
	 * 
	 * ���ܣ��˺����ӿ�֧��Ϊmp4�ļ����ˮӡͼ����Ҫ��ӵ�ˮӡͼƬ��һ��pngͼƬ ������ӵ�λ����mp4��Ƶ�ļ������Ͻ����꣨0,0��
	 * 
	 * @param jin_mp4file
	 *            Դmp4�ļ�
	 * 
	 * @param jadd_logo
	 *            Ҫ��ӵ�ˮӡ��pngͼƬ
	 * 
	 * @param jout_mp4file
	 *            ���ˮӡ���mp4����ļ�
	 * 
	 * @return
	 * 
	 *         ����ֵ: Ϊ 0�ɹ�,Ϊ ��0���� ˵��:
	 *         �˺����ӿ�֧��Ϊmp4�ļ����ˮӡͼ����Ҫ��ӵ�ˮӡͼƬ��һ��pngͼƬ������ӵ�λ����mp4��Ƶ�ļ������Ͻ����꣨0,0��
	 * 
	 * @sample �� / mp4�µ�Mp4.mp4�ļ������/logo/05.pngˮӡ
	 *         FFmpegConvertGMp4AndLogo(��/mp4/Mp4.mp4��, ��/logo/05.png��,
	 *         /mp4/logo.mp4); �����ɻ���/mp4�����ɴ���ˮӡ��logo.mp4��Ƶ�ļ�
	 * 
	 */

	public native int FFmpegConvertGMp4AndLogo(String jin_mp4file,
			String jadd_logo, String jout_mp4file);

	/**
	 * FFmpegConvertGMp4ToMp3 ���ܣ���mp4�ļ�����ȡ����Ƶ�����mp3�ļ�
	 * 
	 * 
	 * @param jin_mp4file
	 *            Դmp4�ļ�
	 * @param jstartTimer
	 *            ��ʼλ��(����Դmp4�ļ����Ǹ�λ�ÿ�ʼ���ֱ�ʾ��ʽ��1.ʱ����00:00:03��2.ֱ��д������) ��ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд���ɣ�
	 * 
	 * @param jlength
	 *            ��ȡ���ȣ�����ȡ��Ƶ��Դmp4�ļ���jstartTimer����ʼjlength�ĳ��ȵ�����������λ�롣 (ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд����)
	 * 
	 * @param jout_mp3file
	 * 
	 *            ��ȡ���Ҫ����ɵ�mp3�ļ�
	 * 
	 * @return 0�ɹ�,Ϊ ��0���� ˵��: �˺����ӿ�֧�ִ�mp4��Ƶ�ļ�����ȡ��ȫ����һ���ֵ���Ƶ�����mp3�����ļ�
	 * 
	 * @sample
	 * 
	 *         �� / mp4�µ�Mp4.mp4��Ƶ�ļ�����ȡ���ӵ�3�봦��ʼ��8�����Ƶ�����/mp3/mp3�����ļ�
	 *         FFmpegConvertGMp4ToMp3(��/mp4/Mp4.mp4��, ��00:00:03�� , ��5��,
	 *         ��/mp3/mp3.mp3��); ���� FFmpegConvertGMp4ToMp3(��/mp4/Mp4.mp4��, ��3�� ,
	 *         ��5��, ��/mp3/mp3.mp3��); ���� ���� ��NULL����ʾ��ʹ�ô˲���
	 *         FFmpegConvertGMp4ToMp3(��/mp4/Mp4.mp4��, ��NULL�� , ��NULL��,
	 *         ��/mp3/mp3.mp3��); ִ���ĳɻ���/mp3������mp3.mp3�����ļ�
	 * 
	 */
	public native int FFmpegConvertGMp4ToMp3(String jin_mp4file,
			String jstartTimer, String jlength, String jout_mp3file);

	/**
	 * ���ܣ���������mp4��Ƶ�ļ��������
	 * 
	 * @param jin_mp4file
	 *            Դmp4�ļ�
	 * 
	 * @param jstartTimer
	 *            ��ʼλ��(��Ҫ��ӵ�������jadd_soundfile(mp3�����ļ�)���Ǹ�λ�ÿ�ʼ�����ֱ�ʾ��ʽ��1.ʱ����00:00
	 *            :03��2.ֱ��д������) ��ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд���ɣ�
	 * 
	 * @param jlength
	 *            ��ӵ���Ƶ���ȣ�����ӵ�������jadd_soundfile(mp3�����ļ�)
	 *            jstartTimer����ʼjlength�ĳ��ȵ�����������λ�� ��ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд���ɣ�
	 * 
	 * @param jadd_soundfile
	 *            Ҫ��ӵ������ļ���mp3�ļ���
	 * 
	 * @param jout_mp4file
	 *            ���������ɺ����ɴ�������mp4��Ƶ�ļ�
	 * 
	 * @return Ϊ 0�ɹ�,Ϊ ��0���� ˵��: �˺����ӿ�֧��Ϊ������mp4�ļ����һ��������
	 * 
	 * @sample
	 * 
	 *         Ϊ / mp4�µ�Mp4.mp4��Ƶ�ļ����һ������
	 *         ��/mp3/mp3.mp3�����ļ��ĵ�3�봦��ʼ��8��Ĳ������ɵ�mp4��Ƶ�ļ������/mp4/outmp4.mp4��Ƶ�ļ�
	 *         FFmpegConvertGMp4AndAcc(��/mp4/Mp4.mp4��, ��00:00:03�� , ��5��,
	 *         ��/mp3/mp3.mp3��,��/mp4/outmp4.mp4��); ����
	 *         FFmpegConvertGMp4AndAcc(��/mp4/Mp4.mp4��, ��3�� , ��5��,
	 *         ��/mp3/mp3.mp3��,��/mp4/outmp4.mp4��); ����
	 * 
	 *         ���� ��NULL����ʾ��ʹ�ô˲��� FFmpegConvertGMp4AndAcc(��/mp4/Mp4.mp4��, ��NULL��
	 *         , ��NULL��, ��/mp3/mp3.mp3��,��/mp4/outmp4.mp4��);
	 *         ִ���ĳɻ���/mp4�����ɴ���������mp4.mp4��Ƶ�ļ�
	 */
	public native int FFmpegConvertGMp4AndAcc(String jin_mp4file,
			String jstartTimer, String jlength, String jadd_soundfile,
			String jout_mp4file);

	/**
	 * 
	 * ���ܣ���һ��mp4��Ƶ�ļ��н�ȡ����һ�α���ɳ�mp4�ļ�ͬʱ���������˵�
	 * 
	 * @param jin_mp4file
	 *            Դmp4�ļ�
	 * @param jstartTimer
	 *            ��ʼλ��(����Դmp4�ļ����Ǹ�λ�ÿ�ʼ���ֱ�ʾ��ʽ��1.ʱ����00:00:03��2.ֱ��д������) �� ��ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд���ɣ�
	 * 
	 * @param jlength
	 *            ��ȡ���ȣ�����ȡ��Ƶ��Դmp4�ļ���jstartTimer����ʼjlength�ĳ��ȵ���Ƶ������λ�룬 ��ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд���ɣ�
	 * 
	 * @param jout_mp4file
	 *            ��ȡ��ɺ�������������mp4��Ƶ�ļ�
	 * 
	 * @param flag
	 *            �Ƿ�ͬʱ���˵�������YES�����˵���NO�����������������˵���
	 * 
	 * @return Ϊ 0�ɹ�,Ϊ ��0���� ˵��: �˺����ӿ�֧�ִ�һ��mp4��Ƶ�ļ��н�ȡ����һ�α���ɳ�mp4�ļ�ͬʱ���������˵�
	 * 
	 * @sample
	 * 
	 *         ��ȡ / mp4�µ�Mp4.mp4��Ƶ�ļ��ĵ�3�봦��ʼ��8��ĵ���Ƶ�����˵�����������ȡ���ɵ�mp4��Ƶ�ļ������/mp4/
	 *         outmp4.mp4��Ƶ�ļ� FFmpegConvertGMp4ToMp4 (��/mp4/Mp4.mp4��, ��00:00:03��
	 *         , ��5��, ��/mp4/outmp4.mp4������YES��); // �������� ���� ��������
	 *         FFmpegConvertGMp4ToMp4 (��/mp4/Mp4.mp4��, ��3�� , ��5��,
	 *         ��/mp4/outmp4.mp4��,��NO��); ִ���ĳɻ���/mp4�����ɽ�ȡ��������������outmp4.mp4��Ƶ�ļ�
	 * 
	 */
	public native int FFmpegConvertGMp4ToMp4(String jin_mp4file,
			String jstartTimer, String jlength, String jout_mp4file, String flag);

	/**
	 * 
	 * ���ܣ���һ��mp4��Ƶ�ļ�����ȡ֡�������pngͼƬ��jpgͼƬ
	 * 
	 * @param jin_mp4file
	 *            Դmp4�ļ�
	 * 
	 * @param jstartTimer
	 *            ��ʼλ��(����Դmp4�ļ����Ǹ�λ�ÿ�ʼ���ֱ�ʾ��ʽ��1.ʱ����00:00:03��2.ֱ��д������)�� ��ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд���ɣ�
	 * 
	 * @param jlength
	 *            ��ȡ��Ƶ���ȣ�����ȡ֡��Դmp4�ļ���jstartTimer����ʼjlength�ĳ��ȵ���Ƶ�ڣ�����λ�롣 ��ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд���ɣ�
	 * 
	 * @param jframerate
	 *            ֡�ʣ���һ����ȡ��֡�� ��ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд���ɣ�
	 * 
	 * @param jout_jpgOpngfile
	 *            �����ͼƬ(�����ͼƬ�ı�﷽ʽimg%2d.jpg, ����ʾ���ɵ�ͼƬ��img00.jpg��img01.jpg ��
	 *            �ȣ����� img%3d.jpg,����ʾ���ɵ�ͼƬ��img000.jpg��img001.jpg �� ��)
	 * 
	 * 
	 * @return Ϊ 0�ɹ�,Ϊ ��0���� ˵��:
	 *         �˺����ӿ�֧�ִ�һ��mp4��Ƶ�ļ�����ȡ֡�������pngͼƬ��jpgͼƬ���������ǰ�һ���Ĺ������еġ�
	 * 
	 * @sample
	 * 
	 *         ��/ mp4�µ�Mp4.mp4��Ƶ�ļ��ĵ�3�봦��ʼ��8��ĵ���Ƶÿ������ȡ��֡�����/photo/��һϵ��ͼƬ��
	 *         FFmpegConvertGMp4ToJpgOPng(��/mp4/Mp4.mp4��, ��00:00:03�� , ��5�� ,
	 *         ��2��, ��/photo/image%2d.jpg��); ����
	 *         FFmpegConvertGMp4ToJpgOPng(��/mp4/Mp4.mp4��, ��3�� , ��5�� , ��2��,
	 *         ��/photo/image%2d.jpg��);
	 *         ִ�гɹ�����/photo/������һϵ��ͼƬ��image00.jpg��image01.jpg��image02.jpg ��
	 * 
	 */
	public native int FFmpegConvertGMp4ToJpgOPng(String jin_mp4file,
			String jstartTimer, String jlength, String jframerate,
			String jout_jpgOpngfile);

	/**
	 * 
	 * ���ܣ���һϵ�а�һ������������ͼƬ��һ��֡�ʺϳ�һ��mp4�ļ�������
	 * 
	 * @param jin_jpgfile
	 *            ��һ������������ͼƬ
	 * 
	 * @param jframerate
	 *            ֡�ʣ���һ�뼸֡�� ��ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд���ɣ�
	 * 
	 * @param jout_mp4file
	 *            ���� jframerate ֡�ʺϳɵ�mp4 �ļ�
	 * 
	 * @return ����ֵ: Ϊ 0�ɹ�,Ϊ ��0���� ˵��: �˺����ӿ�֧�ֽ�һϵ�а�һ������������ͼƬ��һ��֡�ʺϳ�һ��mp4�ļ�������
	 * 
	 * @sample
	 * 
	 *         ��/photo/�°���һ���������ɵ�һϵ��ͼƬ����ÿ����֡���ʺϳ�/mp4/�µ�mp4.mp4��Ƶ�ļ���
	 *         FFmpegConvertGMJpgOPngToMp4 (��/photo/image%2d.jpg��, ��2��,
	 *         ��/mp4/Mp4.mp4��); ���� FFmpegConvertGMJpgOPngToMp4
	 *         (��/photo/image%2d.jpg��, ��2��, ��/mp4/Mp4.mp4��);
	 *         ִ�гɹ�����/mp4/������mp4.mp4��Ƶ�ļ�ÿ�벥����֡
	 * 
	 */
	public native int FFmpegConvertGMJpgOPngToMp4(String jin_jpgfile,
			String jframerate, String jout_mp4file);

	/**
	 * 
	 * ���ܣ���һϵ�а�һ������������ͼƬ��һ��֡�ʼ���һ��mp3�����ļ��ϳ�һ��mp4��Ƶ�ļ���
	 * 
	 * @param jin_jpgfile
	 *            ��һ������������ͼƬ
	 * 
	 * @param jframerate
	 *            ֡�ʣ���һ�뼸֡���� ��ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд���ɣ�
	 * 
	 * @param jstartTimer
	 *            ��ʼλ��(��Ҫ��ӵ�������jadd_soundfile(mp3�����ļ�)���Ǹ�λ�ÿ�ʼ�����ֱ�ʾ��ʽ��1.ʱ����00:00
	 *            :03��2.ֱ��д������)�� ��ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд���ɣ�
	 * 
	 * @param jlength
	 *            ��ӵ���Ƶ���ȣ�����ӵ�������jadd_soundfile(mp3�����ļ�)
	 *            jstartTimer����ʼjlength�ĳ��ȵ�����������λ�롣 ��ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд���ɣ�
	 * 
	 * @param jadd_soundfile
	 *            Ҫ��ӵ������ļ���mp3�ļ���
	 * 
	 * @param jout_mp4file
	 *            ͼƬ���������ϳɵ�mp4��Ƶ�ļ�
	 * 
	 * @return ����ֵ: Ϊ 0�ɹ�,Ϊ ��0���� ˵��:
	 *         �˺����ӿ�֧�ֽ�һϵ�а�һ������������ͼƬ��һ��֡�ʼ���һ��mp3�����ļ��ϳ�һ��mp4��Ƶ�ļ�
	 * 
	 * @sample
	 * 
	 *         ��/photo/�°���һ���������ɵ�һϵ��ͼƬ����ÿ����֡�����ټ���/mp3�µ�mp3.mp3�ӵ�3�봦��ʼ��8������ϳ�/mp4
	 *         /�µ�mp4.mp4��Ƶ�ļ��� FFmpegConvertGMJpgOPngToMp4
	 *         (��/photo/image%2d.jpg��, ��2��,��00:00:03��, ��5��, ��/mp3/mp3.mp3��,
	 *         ��/mp4/Mp4.mp4��); ���� FFmpegConvertGMJpgOPngToMp4
	 *         (��/photo/image%2d.jpg��, ��2��,��3�� , ��5��, ��/mp3/mp3.mp3��,
	 *         ��/mp4/Mp4.mp4��); ִ�гɹ�����/mp4/������mp4.mp4����������Ƶ�ļ�ÿ�벥����֡
	 * 
	 */
	public native int FFmpegConvertGMJpgOPngAndMp3ToMp4(String jin_jpgfile,
			String jframerate, String jstartTimer, String jlength,
			String jadd_soundfile, String jout_mp4file);

	/**
	 * 
	 * ���ܣ���һ��mp4��Ƶ�ļ��н�ȡ����һ��ת����GIFͼ���ļ�
	 * 
	 * @param jin_mp4file
	 *            Դmp4�ļ�
	 * 
	 * @param jbrate
	 *            ����(���ƻ��ʣ����Բ���1500������Խ�߻���Խ�ã������ļ�Խ��)�� ��ע���˲��� �ɴ����ַ��� ��1��
	 *            ��ʾ��ʹ�ô˲���ʹ��Ĭ�����ʣ������ַ��� ��NULL����ʾ ʹ��1500 ���ʣ����� ��Ϊָ�������ʣ�
	 * 
	 * @param jframerate
	 *            ֡�ʣ���һ�뼸֡���� ��ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд���ɣ�
	 * 
	 * @param jstartTimer
	 *            ��ʼλ��(����Դmp4�ļ����Ǹ�λ�ÿ�ʼ���ֱ�ʾ��ʽ��1.ʱ����00:00:03��2.ֱ��д������)�� ��ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд���ɣ�
	 * 
	 * @param jlength
	 *            ��ȡ���ȣ�����ȡ��Ƶ��Դmp4�ļ���jstartTimer����ʼjlength�ĳ��ȵ���Ƶ������λ�롣 ��ע��
	 *            �ɴ����ַ�����NULL����ʾ��ʹ�ô˲��������Ǵ�����ַ�����NULL������Ϊ�ַ�����NULL������Сд���ɣ�
	 * 
	 * @param jout_giffile
	 *            ��ȡת����ɺ�����GIFͼ���ļ�
	 * 
	 * @return ����ֵ: Ϊ 0�ɹ�,Ϊ ��0���� ˵��: �˺����ӿ�֧�ִ�һ��mp4��Ƶ�ļ��н�ȡ����һ��ת����GIFͼ���ļ���
	 * 
	 * @sample:
	 * 
	 *          ��ȡ /
	 *          mp4�µ�Mp4.mp4��Ƶ�ļ��ӵ�3�봦��ʼ��8��ط�����Ƶת����1500������ÿ��2֡��GIFͼ���ļ������浽/GIF/��.
	 *          FFmpegConvertGMp4ToGif (��/mp4/Mp4.mp4��, ��1500�� , ��2�� ,
	 *          ��00:00:03�� , ��5��, ��/GIF/outgif.gif��); ���� FFmpegConvertGMp4ToGif
	 *          (��/mp4/Mp4.mp4��, ��1500�� , ��2�� , ��3�� , ��5��, ��/GIF/ outgif.gif��);
	 *          ִ���ĳɻ���/ GIF������outgif.gif �ļ�
	 * 
	 */
	public native int FFmpegConvertGMp4ToGif(String jin_mp4file, String jbrate,
			String jframerate, String jstartTimer, String jlength,
			String jout_giffile);

	/**
	 * 
	 * ���ܣ�����һ�������ʶ�mp4�ļ� ��GIF�ļ� ����ת���� mp4��gif�Ļ�ת ������ ���� �����1500���ʿɴ���NULL
	 * 
	 * @param jin_file
	 *            Դmp4�ļ����� ԴGIF�ļ�
	 * 
	 * @param jbrate
	 *            ����(���ƻ��ʣ����Բ���1500������Խ�߻���Խ�ã������ļ�Խ��) ��ע���˲��� �ɴ����ַ��� ��1��
	 *            ��ʾ��ʹ�ô˲���ʹ��Ĭ�����ʣ������ַ��� ��NULL����ʾ ʹ��1500 ���ʣ����� ��Ϊָ�������ʣ�
	 * 
	 * @param jout_file
	 *            ת�����ɵ�mp4�ļ�����GIF�ļ���
	 * 
	 * @return
	 * 
	 *         ����ֵ: Ϊ 0�ɹ�,Ϊ ��0���� ˵��: �˺����ӿ�֧�ֲ���һ�������ʶ�mp4�ļ� ��GIF�ļ� ����ת����
	 * 
	 * @sample:
	 * 
	 *          ��/ mp4�µ�Mp4.mp4��Ƶ�ļ�ת����1500�����ʵ�GIFͼ���ļ������浽/GIF/�µ�outgif.gif
	 *          ��/GIF/�µ�outgif.gif�ļ�ת����1500�����ʵ�mp4��Ƶ�ļ������浽/ mp4�µ�Mp4.mp4
	 *          FFmpegConvertGMChangeBitRate(��/mp4/Mp4.mp4��, ��1500�� ,
	 *          ��/GIF/outgif.gif��); ���� FFmpegConvertGMChangeBitRate(��/GIF/
	 *          outgif.gif��, ��1500�� , ��/mp4/Mp4.mp4��); ���� ��ʹ�� �˲��� ʹ�� Ĭ������
	 *          FFmpegConvertGMChangeBitRate(��/GIF/ outgif.gif��, ��1�� ,
	 *          ��/mp4/Mp4.mp4��); ���� ʹ��1500 ����
	 *          FFmpegConvertGMChangeBitRate(��/GIF/ outgif.gif��, ��NULL�� ,
	 *          ��/mp4/Mp4.mp4��); ִ���ĳɻ���/ GIF������outgif.gif �ļ�������/
	 *          mp4������Mp4.mp4��Ƶ�ļ���
	 * 
	 */
	public native int FFmpegConvertGMChangeBitRate(String jin_file,
			String jbrate, String jout_file);

}
