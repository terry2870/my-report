<%@ page contentType="image/jpeg" %>
<%@ page import="java.awt.Graphics"%>
<%@ page import="java.awt.Color"%>
<%@ page import="java.awt.Font"%>
<%@ page import="java.awt.image.BufferedImage"%>
<%@ page import="java.util.Random"%>
<%@ page import="javax.imageio.ImageIO"%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%!
	Color getRandColor(int fc,int bc){//给定范围获得随机颜色
		Random random = new Random();
		if(fc>255) fc=255;
		if(bc>255) bc=255;
		int r=fc+random.nextInt(bc-fc);
		r= r / 20;
		int g=fc+random.nextInt(bc-fc);
		//int b=fc+random.nextInt(bc-fc);
		int b= 250 ;//+ random.nextInt(100);
		return new Color(r,g,b);
	}
%>
<%
//设置页面不缓存


response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);

// 在内存中创建图象
int width=60, height=20;
BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

// 获取图形上下文


Graphics g = image.getGraphics();

//生成随机类


Random random = new Random();

// 设定背景色


g.setColor(new Color(227,234,244));
g.fillRect(0, 0, width, height);

//设定字体
g.setFont(new Font("Times New Roman",Font.PLAIN,18));

// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到


g.setColor(new Color(168,188,215));
for (int i=0;i<155;i++)
{
	int x = random.nextInt(width);
	int y = random.nextInt(height);
    int xl = random.nextInt(12);
    int yl = random.nextInt(12);
	g.drawLine(x,y,x+xl,y+yl);
}

// 取随机产生的认证码(4位数字)
String sRand="";
for (int i=0;i<4;i++){
	String rand=String.valueOf(random.nextInt(10));
	sRand+=rand;
	// 将认证码显示到图象中
	//g.setColor(new Color(10 + random.nextInt(110), 10 + random.nextInt(110), 100 + random.nextInt(110)));
	g.setColor(new Color(49,65,98));
	//调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成


	g.drawString(rand,13*i+6,16);
}

// 将认证码存入SESSION
request.getSession().setAttribute("checkCode", sRand);

//画边框


g.setColor(Color.black);
g.drawRect(0,0,width-1,height-1);
// 图象生效
g.dispose();

ServletOutputStream sout = null;
// 输出图象到页面


//ImageIO.write(image, "JPEG", response.getOutputStream());
org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(getClass());
try{
	sout = response.getOutputStream();
	ImageIO.write(image, "JPEG", sout);
}catch(Exception e){
	log.error("error to create checkCode!", e);
}finally{
	try{
		if(sout != null){
			sout.close();
		}
		out.clear();
		out = pageContext.pushBody();
	}catch(Exception e){
		log.error("error to close out!", e);
	}
}
%>

