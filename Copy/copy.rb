require 'fileutils'

class Copy
  
  # $src = ARGV[0]
  $src = "/Users/phanisaripalli/Desktop/blah/";
  
  $dst = "/Users/phanisaripalli/Desktop2/blah2.txt";
  # $dst = ARGV[1]
  
  
=begin
 USE CASE
 
 1) if src and dst, do nothing
 2) if dst already exists, do nothing, promt msg for input 
    1) Y/y to copy
     2) N/n to not to copy
   
  
=end      
      
  def dst_valid?
    arr = Array.new
    arr = $dst.split(File::SEPARATOR)
    arr.pop
    pathToCheck = ""
    arr.each { |i|
      pathToCheck += i + "/"
    }
    File.readable?(pathToCheck) and !$dst.include?(".")   
  end
  
  def read_source
    
    if File.exist?($src)
      file = File.open($src, "r")
    else 
      puts "File does not exist"  
    end 
    
  end
  
  def blah
    
  end
  
end

var = Copy.new
var.read_source

if File.exist?($src) && var.dst_valid?
  if ($src.eql? $dst)
    puts "Choose a different destination"
  else
    if File.directory?($src)
      if !$src.end_with?(File::SEPARATOR)
        $src += File::SEPARATOR
      end
      if !Dir.exists?($dst)
        FileUtils.mkdir($dst)
      end
      FileUtils.cp_r(Dir.glob($src + "*"), $dst)
    else
      FileUtils.cp($src, $dst)
    end
  end
else
  puts "Choose a valid destination"  
  
end





