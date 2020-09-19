package cn.csl.basics.util.file;

import java.io.*;

public class FileUtil {

    /**
     * 利用StringBuffer写文件
     * 该方法可以设定使用何种编码，有效解决中文问题。
     * @throws IOException
     */
    public static void StringBuffer(String filename, String content) throws IOException{
        File file=new File(filename);
        if(!file.exists())
            file.createNewFile();
        FileOutputStream out=new FileOutputStream(file,true);
        StringBuffer sb=new StringBuffer();
        sb.append(content);
        out.write(sb.toString().getBytes("utf-8"));
        out.close();
    }
    /**
     *以文件流的方式复制文件
     */
    public static void copyFile(String src,String dest) throws IOException{
        FileInputStream in=new FileInputStream(src);
        File path = new File(dest.substring(0,dest.lastIndexOf("\\")));
        if(!path.exists()) {
            path.mkdirs();
        }
        File file=new File(dest);
        if(!file.exists())
            file.createNewFile();
        FileOutputStream out=new FileOutputStream(file);
        int c;
        byte buffer[]=new byte[1024];
        while((c=in.read(buffer))!=-1){
            for(int i=0;i<c;i++)
                out.write(buffer[i]);
        }
        in.close();
        out.close();
    }

    /**
     * 文件重命名
     * @param path
     * @param oldname
     * @param newname
     */
    public static void renameFile(String path,String oldname,String newname){
        if(!oldname.equals(newname)){//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile=new File(path+"/"+oldname);
            File newfile=new File(path+"/"+newname);
            if(newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                System.out.println(newname+"已经存在！");
            else{
                oldfile.renameTo(newfile);
            }
        }
    }

    /**
     * 转移文件目录
     */
    public static void changeDirectory(String filename,String oldpath,String newpath,boolean cover){
        if(!oldpath.equals(newpath)){
            File oldfile=new File(oldpath+"/"+filename);
            File newfile=new File(newpath+"/"+filename);
            if(newfile.exists()){//若在待转移目录下，已经存在待转移文件
                if(cover)//覆盖
                    oldfile.renameTo(newfile);
                else
                    System.out.println("在新目录下已经存在："+filename);
            }
             else{
                oldfile.renameTo(newfile);
            }
        }
    }

    /**
     * 利用FileInputStream读取文件
     */
    public static String FileInputStream(String path) throws IOException{
        File file=new File(path);
        if(!file.exists()||file.isDirectory())
            throw new FileNotFoundException();
        FileInputStream fis=new FileInputStream(file);
        byte[] buf = new byte[1024];
        StringBuffer sb=new StringBuffer();
        while((fis.read(buf))!=-1){
            sb.append(new String(buf));
            buf=new byte[1024];//重新生成，避免和上次读取的数据重复
        }
        return sb.toString();
    }

    /**
     * 利用BufferedReader读取
     */
    public static String BufferedReader(String path) throws IOException{
        File file=new File(path);
        if(!file.exists()||file.isDirectory())
            throw new FileNotFoundException();
        BufferedReader br=new BufferedReader(new FileReader(file));
        String temp=null;
        StringBuffer sb=new StringBuffer();
        temp=br.readLine();
        while(temp!=null){
            sb.append(temp+" ");
            temp=br.readLine();
        }
        return sb.toString();
    }

    public static void createFile(String filename)throws IOException{
        File file = new File(filename);
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        if (!file.exists()){
            file.createNewFile();
        }
    }
    /**
     * 创建文件夹
     * @param path
     * @throws IOException
     */
    public static void makeDir(String path) throws IOException{
        File file=new File(path);
        if(!file.exists())
            file.mkdirs();
    }
    /**
     * 删除文件
     */
    public static void delFile(String path,String filename){
        File file=new File(path+"/"+filename);
        if(file.exists()&&file.isFile())
            file.delete();
    }

    /**
     * 删除目录
     * 要利用File类的delete()方法删除目录时，必须保证该目录下没有文件或者子目录，
     * 否则删除失败，因此在实际应用中，我们要删除目录，
     * 必须利用递归删除该目录下的所有子目录和文件，然后再删除该目录。
     * @param path
     */
    public static void delDir(String path){
        File dir=new File(path);
        if(dir.exists()){
            File[] tmp=dir.listFiles();
            for(int i=0;i<tmp.length;i++){
                if(tmp[i].isDirectory()){
                    delDir(path+"/"+tmp[i].getName());
                }
                 else{
                    tmp[i].delete();
                }
            }
            dir.delete();
        }
    }

    /**
     * 删除目录及目录下的文件
     * @param dir
     * 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = FileUtil.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = FileUtil.deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }
    /**
     * 删除单个文件
     * @param fileName
     * 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

}
