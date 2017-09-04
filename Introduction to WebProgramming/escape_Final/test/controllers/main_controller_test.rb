require 'test_helper'

class MainControllerTest < ActionDispatch::IntegrationTest
  test "should get search" do
    get main_search_url
    assert_response :success
  end

  test "should get report" do
    get main_report_url
    assert_response :success
  end

  test "should get detail" do
    get main_detail_url
    assert_response :success
  end

end
