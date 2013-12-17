class Directory
  
  $option = ARGV[0]
  # $option = "ls";
  
  def curr_dir
    @currentDir = Dir.pwd
  end
  
  directory = Directory.new
  var = ARGV*" "
  
   begin
    if var == "ls"
      Dir.foreach(directory.curr_dir) do |item|
        puts item
      end

    elsif var == "pwd"
      puts directory.curr_dir

    elsif var =~ /^cd\s[\w\W]+\z/
      Dir.chdir(ARGV[1])
      puts Dir.pwd

    elsif var =~/^find\s[\w\W]+\s[\w\W]+\z/
      name = ARGV[1]
      directory = ARGV[2]
      if !directory.end_with?(File::SEPARATOR)
        directory += File::SEPARATOR
      end
      Dir.foreach(directory) do |item|
        if item =~ /#{name}/i
          puts directory + item
        end
      end
    end
      
  rescue
    puts "Invalid expression"
  end
end