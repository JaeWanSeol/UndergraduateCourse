class MainController < ApplicationController
  def index
  end

  def search
  	@resultroom = []

  	if(params.nil?)
  		puts "sibal nil"
  	else
  		@check = params[:box]
  		if(@check.nil?)
  			puts "wow nil"
  		else
  			puts "not nil"
  			@region = []
  			@theme = []
  			@level = []


  			(1..7).each do |i|
  				if(@check[i.to_s] != nil)
  					@region.push(@check[i.to_s])
  				end
  			end
  			(8..10).each do |i|
  				if(@check[i.to_s] != nil)
  					@theme.push(@check[i.to_s])
  				end
  			end
  			(11..15).each do |i|
  				if(@check[i.to_s] != nil)
  					@level.push(@check[i.to_s])
  				end
  			end



  			if((@region.length != 0) && (@theme.length != 0) && (@level.length != 0))
  				#what to do
  				@resultroom = Room.where(region: @region, theme: @theme, level: @level)

  			else
  				puts "there exist n nil"
  			end


  		end
  	end


  	#checked = params[:box].values unless params.nil?

  	#if(checked.nil?)
  	# 	puts "hello"
  	 #	puts checked
  	 #	puts "length is"
  	 #	puts "hi"
  	#end
  end

  def report
  end

  def detail
    @detailRoom = Room.find(params[:id])
    @replies = @detailRoom.replies
  end

  def comments
    @repl = Reply.new()

    if(!params.nil?)
      @check = params[:reply]
      @rating = ""


      #check if Rating is number
      if(isNumber(@check["rating"]))
        @rating = @check["rating"]
        puts "number"
      else
        @rating = "0"
        puts "not number"
      end



      @room = Room.find(params[:room_id])
      if((@room.cnt).nil?)
        @room.cnt = 1
      else
        @room.cnt = @room.cnt + 1
      end

      if((@room.rating).nil?)
        @room.rating = @rating.to_f
      else
        @room.rating = ((@room.rating * (@room.cnt-1)) + @rating.to_f) / @room.cnt
      end

      @room.save

      @repl = @room.replies.create(:usr => @check["usr"], :comment => @check["comment"])
      @repl.rating = @rating


      @repl.save

      if @repl.save
        puts "save good"
      else
        puts "save bad"
      end


    end
    redirect_to main_detail_path(:id => @room.id)
  end

private
def isNumber string
  true if Float(string) rescue false
end

end
